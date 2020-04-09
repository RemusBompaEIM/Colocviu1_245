package ro.pub.cs.systems.eim.Colocviu1_245;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Colocviu1_245MainActivity extends AppCompatActivity {

    private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 2;
    private static final String SUM = "SUMA_SAVED";
    private static final String LAST_TEXT = "TEXT SAVED";
    private static String suma = "";
    private static String last_text = "";
    private static AppCompatActivity main = null;

    private String serviceStatus;
    private static final String SERVICE_STARTED = "SERVICE_STARTED";

    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_245_main);
        main = this;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SUM))
                suma = savedInstanceState.getString(SUM);
            if (savedInstanceState.containsKey(LAST_TEXT))
                last_text = savedInstanceState.getString(LAST_TEXT);

        }

        Button addB = (Button)findViewById(R.id.add);
        addB.setOnClickListener(new AddButtonClickListener());
        Button computeB = (Button)findViewById(R.id.compute);
        computeB.setOnClickListener(new AddButtonClickListener());
        intentFilter.addAction("action_type");

    }

     class AddButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch(view.getId()) {
                case R.id.add:
                    String text = ((TextView)findViewById(R.id.all_terms)).getText().toString();
                    String nr = ((EditText)findViewById(R.id.next_term)).getText().toString();
                    if(!nr.isEmpty())
                        ((TextView)findViewById(R.id.all_terms)).setText(text + "+" + nr);
                    break;
                case R.id.compute:
                    String ints = ((TextView)findViewById(R.id.all_terms)).getText().toString();
                    if(ints.equals(last_text))
                        Toast.makeText(main, "The activity returned " + suma, Toast.LENGTH_LONG).show();
                    else {
                        Intent intent = new Intent(getApplicationContext(), Colocviu1_245SecondaryActivity.class);
                        intent.putExtra(Colocviu1_245SecondaryActivity.ALL_TERMS_KEY, ints);
                        startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    }
                    break;
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Broadcast", intent.getStringExtra("Broadcast"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                suma = intent.getStringExtra(Colocviu1_245SecondaryActivity.RESULT_KEY);
                Toast.makeText(this, "The activity returned " + suma, Toast.LENGTH_LONG).show();
                if(Integer.parseInt(suma) > 10){
                    Intent serv_intent = new Intent(getApplicationContext(), Colocviu245_Service.class);
                    serv_intent.putExtra(SUM, suma);
                    getApplicationContext().startService(intent);
                    serviceStatus = SERVICE_STARTED;
                }
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SUM, suma);
        savedInstanceState.putString(LAST_TEXT, last_text);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu245_Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
}
