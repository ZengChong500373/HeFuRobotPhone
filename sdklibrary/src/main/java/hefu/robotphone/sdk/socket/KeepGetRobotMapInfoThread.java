package hefu.robotphone.sdk.socket;

public class KeepGetRobotMapInfoThread extends Thread {

    @Override
    public void run() {
        RobotMapSocket socket= RobotMapSocket.getInstance();
        socket.write();
        while (true){
            try
            {
                Thread.sleep(200);
                if (socket.getReady()){
                    socket.getGetRebotInfo2();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
