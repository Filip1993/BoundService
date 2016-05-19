package com.filipkesteli.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BoundService extends Service {

    //inner klasa- javno je dostupna -> binder koji prica prilikom runtime-a i dolazi do servisa
    public class BoundServiceBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }

    private final BoundServiceBinder boundServiceBinder = new BoundServiceBinder();

    //interface IBinder
    @Override
    public IBinder onBind(Intent intent) {
        return boundServiceBinder;
    }

    private Thread thread;
    private int counter;

    @Override
    public void onCreate() {
        //startat ce samo counter -> pitat cemo servis gdje je u nekom trenutku
        super.onCreate();
        startCounter();
    }

    private void startCounter() {
        //moramo sami kreirati svoj Thread i startati:
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //zauvijek ce radit dok god ga netko ne ubije!!
                while (true) {
                    counter++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }); //ovaj thread jos nist ne radi -> moramo mu gurnuti runnable
        thread.start();
    }

    //otvaramo se s public metodom -> dohvati mi counter izvana
    public String getCounterValue() {
        return (counter + "");
    }

    @Override
    public void onDestroy() {
        //Ubijamo thread (interruptamo ga) -> ako thread postoji dok pozovemo stop metodu, ubij ga
        //inace jede rasurse bezveze
        if (thread != null) {
            thread.interrupt();
        }
        super.onDestroy();
    }
}

