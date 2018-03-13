package hefu.robotphone.sdk.socket;


import android.text.TextUtils;

public class DirectionControlThread extends Thread {
    public String where;
    public void  go(String where){
        this.where=where;
    }
    @Override
    public void run() {
        RobotCmdSocket robotControlSocket= RobotCmdSocket.getInstance();
        while (true){
            if (robotControlSocket.getReady()){
                try
                {
                    Thread.sleep(300);
                    if (!TextUtils.isEmpty(where)){
                        robotControlSocket.sendMsg(where);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
