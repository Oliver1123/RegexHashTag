package oliver.example.com.rejextext;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String HASH = "#";
    private static final String TAG = "MAIN";
    private EditText mEditText;
    private TextView mResultTextView;
    private final String[] mRandomString = {
            "test #hash1 test #hash2",
            "test #hash1test#hash2",
            "test #hash1 test#hash2",
            "test #hash1, test #hash2",
            "test #hash1. test #hash2",
            "test #hash1" +
                    " test #hash2",

            "#hash1 test #hash2, test2 #hash3. test3\n#hash4 #hash5#hash6",
            "test #hash2, test2 #hash3. test3\n#hash4 #hash5#hash6 test",


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAction();
            }
        });

        findViewById(R.id.btnRandom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filRandom();
            }
        });

        mEditText = (EditText) findViewById(R.id.etDescription);
        mResultTextView = (TextView) findViewById(R.id.tvResult);
    }

    private void filRandom() {
        Random r = new Random();
        mEditText.setText(mRandomString[r.nextInt(mRandomString.length)]);
    }

    private void goAction() {
        String text = mEditText.getText().toString();
        // this step is mandated for the url and clickable styles.
        mResultTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mResultTextView.setText(getHashTags(text));
    }

    private SpannableStringBuilder getHashTags(String str) {
        Log.d(TAG, "getHashTags " + str);
        String regex = "#([\\w]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        while (matcher.find()) {
            String hashTag = matcher.group();
            Log.d(TAG, "matcher find " + hashTag);
            int start = -1;
            while ((start = str.indexOf(hashTag, start + 1)) != -1) {
                Log.d(TAG, " start : " + start);
                ssb.setSpan(new MyClickableSpan(hashTag), start, start + hashTag.length(), 0);
            }
        }

        return ssb;
    }

    public class MyClickableSpan extends ClickableSpan {

        private final String mHashTag;

        public MyClickableSpan(String word) {
            mHashTag = word;
        }

        @Override
        public void onClick(View widget) {
            Log.d(TAG, "onClick " + mHashTag);
            Toast.makeText(widget.getContext(), "On Click \'" + mHashTag + "\'", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
        }
    }
}
