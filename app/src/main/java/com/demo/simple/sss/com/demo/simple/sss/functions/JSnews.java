package com.demo.simple.sss.com.demo.simple.sss.functions;

/**
 * Created by Administrator on 2016/4/12.
 */
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.demo.simple.sss.MainActivity;

public class JSnews {
        private MainActivity ma;

        public JSnews(MainActivity context) {
            this.ma = context;
        }
        @JavascriptInterface
        public void showMsg(String msg) {
            Toast.makeText(ma, msg, Toast.LENGTH_SHORT).show();
        }
}
