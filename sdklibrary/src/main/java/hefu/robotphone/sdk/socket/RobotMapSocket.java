package hefu.robotphone.sdk.socket;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.utlis.Constant;

public class RobotMapSocket {
    Socket socket = null;

    OutputStreamWriter outputStreamWriter = null;
    private int port = 9003;
    public static String TAG = "RobotCmdSocket";
    private static RobotMapSocket instance = null;
    private Boolean isReady = false;
    private Thread mThread=null;
    private RobotMapSocket() {
    }

    public static RobotMapSocket getInstance() {
        if (instance == null) {
            instance = new RobotMapSocket();
        }
        return instance;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    private String hostName;

    public void start() {
        //TODO 这里存在线程安全问题
        if (mThread!=null){
            return;
        }
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (getReady()==false){
                    try {
                        Thread.sleep(500);
                        ConnectServer();
                        keepGetRebotMapInfo();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isReady=false;
                    }

                }
            }
        });
        mThread.start();
    }

    public void keepGetRebotMapInfo() {
        if (getReady()) {
            KeepGetRobotMapInfoThread keepGetRobotMapInfoThread = new KeepGetRobotMapInfoThread();
            keepGetRobotMapInfoThread.start();
        }
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
//            reader = new BufferedInputStream(socket.getInputStream());

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            isReady = true;
        } catch (Exception e) {
            Log.e("jyh_error", e.toString() + "");
            isReady = false;
        }
    }

    public void write() {
        try {
            outputStreamWriter.write("2 0 086 " + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL);//ceshi
            outputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStreamWriter.write("2 0 08B " + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL);//ceshi
            outputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int REPLY_MAX_LENGTH = 1024 * 10;
    private static BufferedInputStream reader;

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

            for (String str : strs) {
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

    int length = 0;
    String allString = "";
    String partString = "";
    private char buf[] = new char[REPLY_MAX_LENGTH];
    InputStreamReader inputStreamReader = null;

    public void getGetRebotInfo2() {
        length = read(buf, 0, REPLY_MAX_LENGTH - 1);
        if (length > 0) {
            partString = new String(buf, 0, length);
            allString += partString;
            Log.d("common2", allString + "");
            int aCommonEnd = 0;//一条指令结束位置
            aCommonEnd = allString.indexOf(Constant.BUF_INSTRUCTION_SPLIT_SYMBOL);
            while (aCommonEnd >= 0) {
                String value = allString.substring(0, aCommonEnd);
                Log.d("value", value + "");
                if (callBack != null) {
                    callBack.RobotInfoSuccess(value);
                }
                Log.d("aCommonEnd", aCommonEnd + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL.length() + "allString.length():" + allString.length());
                if (aCommonEnd + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL.length() >= allString.length()) {
                    allString = "";
                    aCommonEnd = 0;
                    break;
                }
                allString = allString.substring(aCommonEnd + Constant.BUF_INSTRUCTION_SPLIT_SYMBOL.length(), allString.length());
                aCommonEnd = allString.indexOf(Constant.BUF_INSTRUCTION_SPLIT_SYMBOL);
            }
        }
    }

    public int read(char buf[], int offset, int maxLength) {
        int length = 0;
        if (inputStreamReader != null) {
            try {
                length = inputStreamReader.read(buf, offset, maxLength);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return length;
    }

    public Boolean getReady() {
        return isReady;
    }

    RobotInfoCallBack callBack;

    public void setCallBack(RobotInfoCallBack callBack) {
        this.callBack = callBack;
    }
}
