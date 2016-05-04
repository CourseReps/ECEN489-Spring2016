package com.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


/**
 * Created by Chaance on 4/29/2016.
 */
public class JavascriptInterfaceActivity extends Activity {

    WebView wv;

    JavaScriptInterface JSInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wv = (WebView)findViewById(R.id.webview);

        wv.getSettings().setJavaScriptEnabled(true);
        // register class containing methods to be exposed to JavaScript

        JSInterface = new JavaScriptInterface(this);
        wv.addJavascriptInterface(JSInterface, "JSInterface");

        wv.loadUrl("file:///C:/Users/Chaance/Documents/ECEN%202016/ECEN%20489%20(Mobile%20Sensing)/Lunafinder%20calculator.html");

    }


    public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void changeActivity()
        {
            Intent i = new Intent(JavascriptInterfaceActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
