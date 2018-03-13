package hefu.robotphone.sdk.socket;

public class KeepGetRobotInfoThread extends Thread {

    @Override
    public void run() {
        RobotCmdSocket robotControlSocket= RobotCmdSocket.getInstance();
        while (true){
            try
            {
                Thread.sleep(2000);
                if (robotControlSocket.getReady()){
                    robotControlSocket.getGetRebotInfo();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
