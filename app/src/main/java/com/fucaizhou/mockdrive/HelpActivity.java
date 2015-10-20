package com.fucaizhou.mockdrive;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by fucai.zhou on 2015/10/15.
 */
public class HelpActivity extends Activity {

    public TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_activity);

        mTextView = (TextView) findViewById(R.id.help_text);
    }
}
