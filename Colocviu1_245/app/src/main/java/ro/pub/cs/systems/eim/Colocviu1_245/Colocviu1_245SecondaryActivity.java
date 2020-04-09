package ro.pub.cs.systems.eim.Colocviu1_245;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Colocviu1_245SecondaryActivity extends AppCompatActivity {

    public static final String ALL_TERMS_KEY = "ALL_TERMS_KEY";
    public static final String RESULT_KEY = "RESULT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            String ints = intent.getStringExtra(ALL_TERMS_KEY);
            String[] ls = ints.split("\\+");
            int res = 0;
            for(int i = 0; i<= ls.length; i++) {
                res += Integer.parseInt(ls[i]);
            }
            Intent intentToParent = new Intent();
            intent.putExtra(RESULT_KEY, res);
            setResult(RESULT_OK, intentToParent);
            finish();
        }
    }
}
