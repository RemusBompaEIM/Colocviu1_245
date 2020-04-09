package ro.pub.cs.systems.eim.Colocviu1_245;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;

public class Colocviu245_Service extends Service {
    private ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String suma = intent.getStringExtra("SUMA");
        processingThread = new ProcessingThread(this, suma);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }

    public Colocviu245_Service() {
    }

    public class ProcessingThread extends Thread {

        private Context context = null;
        private String suma = "";
        private boolean isRunning = true;


        public ProcessingThread(Context context, String suma) {
            this.context = context;
            this.suma = suma;
        }

        @Override
        public void run() {
            while (isRunning) {
                sendMessage();
                sleep();
            }
        }

        private void sendMessage() {
            Intent intent = new Intent();
            intent.setAction("action_type");
            intent.putExtra("Broadcast",
                    new Date(System.currentTimeMillis()) + " " + suma);
            context.sendBroadcast(intent);
        }

        private void sleep() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        public void stopThread() {
            isRunning = false;
        }
    }
}
