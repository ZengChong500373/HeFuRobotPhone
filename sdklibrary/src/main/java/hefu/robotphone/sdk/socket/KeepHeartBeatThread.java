package hefu.robotphone.sdk.socket;


import hefu.robotphone.sdk.utlis.ByteUtil;
import hefu.robotphone.sdk.utlis.CodeInstructionSet;
import hefu.robotphone.sdk.utlis.MyLog;
import hefu.robotphone.sdk.utlis.SystemInfoUtil;

public class KeepHeartBeatThread extends Thread {
    @Override
    public void run() {
        RobotCmdSocket robotControlSocket= RobotCmdSocket.getInstance();
        String keepMsg = "0 "+ SystemInfoUtil.getMac()+" 4"+ ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_HEARTBEAT), "");
        while (true){
            try
            {
                Thread.sleep(20000);        //keep live at least once per minute
                if (robotControlSocket.getReady()){
                    robotControlSocket.sendMsg(keepMsg);

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
