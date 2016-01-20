package com.zxjdev.demo.spannable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mEtSpannable;
    private TextView mTvSpannable;
    private PlatoSpan[] mSpans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtSpannable = (EditText) findViewById(R.id.et_spannable);
        mTvSpannable = (TextView) findViewById(R.id.tv_spannable);

        PlatoSpan span1 = new PlatoSpan() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "刀无极 click");
            }
        };
        span1.userid = "123";

        PlatoSpan span2 = new PlatoSpan() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "醉饮黄龙 click");
            }
        };
        span2.userid = "234";

        PlatoSpan span3 = new PlatoSpan() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Andrew click");
            }
        };
        span3.userid = "345";

        SpannableString ss = new SpannableString("@刀无极 和@醉饮黄龙 是兄弟@Andrew");
        ss.setSpan(span1, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 6, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 15, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEtSpannable.setText(ss);

        mEtSpannable.setMovementMethod(LinkMovementMethod.getInstance());
        mEtSpannable.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        int selectionStart = mEtSpannable.getSelectionStart();
                        int selectionEnd = mEtSpannable.getSelectionEnd();

                        Log.d(TAG, "del click! cursor start: " + selectionStart + " cursor end: " + selectionEnd);
                        PlatoSpan[] spans = mEtSpannable.getText().getSpans(0, mEtSpannable.length(), PlatoSpan.class);
                        for (PlatoSpan span : spans) {
                            int spanStart = mEtSpannable.getText().getSpanStart(span);
                            int spanEnd = mEtSpannable.getText().getSpanEnd(span);
                            Log.d(TAG, "Span start: " + spanStart);
                            Log.d(TAG, "Span end: " + spanEnd);
                            if (selectionStart == spanStart && selectionEnd == spanEnd) {
                                return false;
                            }
                            if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                                mEtSpannable.setSelection(spanStart, spanEnd);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });

        mTvSpannable.setMovementMethod(LinkMovementMethod.getInstance());
        updateOutput(ss);

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOutput(mEtSpannable.getText());
            }
        });

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert("@柏拉图");
            }
        });
    }

    public void updateOutput(Spannable ss) {
        PlatoSpan[] spans1 = ss.getSpans(0, mEtSpannable.length(), PlatoSpan.class);

        // 对span对象进行排序，在字符串靠前的排在前面
        int position;
        for (int i = 0; i < spans1.length; i++) {
            int j = i + 1;
            position = i;
            PlatoSpan temp = spans1[i];
            for (; j < spans1.length; j++) {
                if (ss.getSpanStart(spans1[j]) < ss.getSpanStart(temp)) {
                    temp = spans1[j];
                    position = j;
                }
            }
            spans1[position] = spans1[i];
            spans1[i] = temp;
        }

        StringBuffer sb = new StringBuffer(ss);

        for (int i = spans1.length; i > 0; i--) {
            int spanStart = ss.getSpanStart(spans1[i - 1]);
            int spanEnd = ss.getSpanEnd(spans1[i - 1]);
            sb.replace(spanStart, spanEnd, "<" + spans1[i - 1].userid + ">");
        }
        mTvSpannable.setText(sb.toString());
    }

    public void insert(String text) {
        int start = mEtSpannable.getSelectionStart();
        mEtSpannable.getText().insert(start, text);
        PlatoSpan span = new PlatoSpan() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "柏拉图 click");
            }
        };
        span.userid = "789";
        mEtSpannable.getText().setSpan(span, start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
