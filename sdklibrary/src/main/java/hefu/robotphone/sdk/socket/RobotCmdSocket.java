package hefu.robotphone.sdk.socket;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.utlis.Constant;
import hefu.robotphone.sdk.utlis.CrashHandler;
import hefu.robotphone.sdk.utlis.SystemInfoUtil;
import hefu.robotphone.sdk.utlis.ThreadManager;


public class RobotCmdSocket {
    public static String TAG = "RobotCmdSocket";
    private static RobotCmdSocket instance = null;
    //socket相关配置
    private Socket socket;
    private static Writer writer;
    private static BufferedInputStream reader;
    private Boolean isReady = false;
    private DirectionControlThread directionControlThread;
    private int REPLY_MAX_LENGTH = 1024;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRobotInfoCallBack(RobotInfoCallBack callBack) {
        this.callBack = callBack;
    }

    RobotInfoCallBack callBack;
    private String hostName;
    private int port = 9001;
    private Thread mThread=null;
    private RobotCmdSocket() {
    }

    public static RobotCmdSocket getInstance() {
        if (instance == null) {
            instance = new RobotCmdSocket();
        }
        return instance;
    }


    public void start() {
        //TODO 这里存在线程安全问题
        if (mThread!=null){
            return;
        }
        mThread=   new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (getReady()==false){
                        Thread.sleep(500);
                        ConnectServer();
                        keepAlive();
                        keepGetRebotInfo();
                        initDirection();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isReady=false;
                    mThread=null;
                }
            }
        });
        mThread.start();
    }


    /**
     * 链接服务器
     */
    public void ConnectServer() {
        try {
            String host = InetAddress.getByName(hostName).getHostAddress();
            socket = new Socket(host, port);
            socket.setSoTimeout(0);
            socket.setKeepAlive(true);
            socket.setOOBInline(true);
            writer = new OutputStreamWriter(socket.getOutputStream());
            reader = new BufferedInputStream(socket.getInputStream());
            isReady = true;
            String sendStr = "speakTTS" + " " + SystemInfoUtil.getMac() + " 4" + " "+ " " + "888"+ " " + "链接成功" ;
            sendMsg(sendStr);
        } catch (Exception e) {
            Log.e("jyh_error", e.toString() + "");
            isReady = false;
        }

    }

    public Boolean getReady() {
        return isReady;
    }


    public void goWhere(String str) {
        if (directionControlThread != null) {

            directionControlThread.go(str);
        } else {

        }

    }

    public void robotCmd(final String speak) {
        ThreadManager.getInstance().addRun(new Runnable() {
            @Override
            public void run() {
                sendMsg(speak);
            }
        });
    }

    protected void sendMsg(String cmd) {
        cmd += Constant.BUF_INSTRUCTION_SPLIT_SYMBOL;
        try {
            if (writer != null && !TextUtils.isEmpty(cmd)) {
                Log.e("jyh_sendMsg", cmd);
                writer.write(cmd, 0, cmd.length());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            CrashHandler.getInstance().saveCrashInfo2File(e);
            Log.e("jyh_error", e.toString());
        }
    }

    /**
     * 服务器心跳连接
     */
    public void keepAlive() {
        if (getReady()) {
            KeepHeartBeatThread keepHeartBeatThread = new KeepHeartBeatThread();
            keepHeartBeatThread.start();
        }
    }

    public void keepGetRebotInfo() {
        if (getReady()) {
            KeepGetRobotInfoThread keepGetRobotInfoThread = new KeepGetRobotInfoThread();
            keepGetRobotInfoThread.start();
        }
    }

    private void initDirection() {
        if (getReady()) {
            directionControlThread = new DirectionControlThread();
            directionControlThread.start();
        }
    }

    public void getGetRebotInfo() {
        //初始化获取弹幕服务器返回信息包大小
        byte[] recvByte = new byte[REPLY_MAX_LENGTH];
        //定义服务器返回信息的字符串
        String dataStr;
        try {
            //读取服务器返回信息，并获取返回信息的整体字节长度
            int recvLen = reader.read(recvByte, 0, recvByte.length);

            //根据实际获取的字节数初始化返回信息内容长度
            byte[] realBuf = new byte[recvLen];
            //按照实际获取的字节长度读取返回信息
            System.arraycopy(recvByte, 0, realBuf, 0, recvLen);
            //根据TCP协议获取返回信息中的字符串信息
            dataStr = new String(realBuf, 0, realBuf.length);
            String[] strs = dataStr.split("\\" + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL);


            for (String str :
                    strs) {

                if (callBack != null) {
                    callBack.RobotInfoSuccess(str);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.RobotInfoFail();
            }

        }
    }
}
