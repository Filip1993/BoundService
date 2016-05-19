package com.filipkesteli.boundservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //referenca prema BoundService-u:
    private BoundService boundService;
    private boolean serviceBound;

    //inline implementacija klase:
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //ovaj ce mi pomoci da se docepam service-a:
            BoundService.BoundServiceBinder boundServiceBinder = (BoundService.BoundServiceBinder) binder;
            boundService = boundServiceBinder.getService(); //kolki ti je counter value sad mogu pitati service
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //poslovna logika:
    public void startService(View view) {
        //Moramo ga startati i bindati (servis):
        if (!serviceBound) {
            Intent intent = new Intent(this, BoundService.class);
            startService(intent);

            //iz Activityja:
            //Uzmem si iz servisa:
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            serviceBound = true;
        }
    }

    public void stopService(View view) {
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            Intent intent = new Intent(this, BoundService.class);
            stopService(intent); //moramo ga stoppirati jer moramo ubiti onaj Thread!!!
        }
    }

    public void getCounterValue(View view) {
        //servis je inicijaliziran ako je BIND-an:
        if (serviceBound) {
            String counterValue = boundService.getCounterValue();
            Toast.makeText(MainActivity.this, counterValue + "", Toast.LENGTH_SHORT).show();
        }
    }
}
