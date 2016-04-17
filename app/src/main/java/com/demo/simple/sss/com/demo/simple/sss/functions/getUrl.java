package com.demo.simple.sss.com.demo.simple.sss.functions;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.demo.simple.sss.*;

/**
 * Created by Administrator on 2016/4/11.
 */
public class getUrl {
    String Url;
    String TAG = "getUrl______________";

    AVFile html;
    Boolean STATE_TRUE = true;
    Boolean STATE_FALSE = false;
    Boolean UPDATE = false;
    int s = 1;

    public String getUrl() {
        AVObject post;
        post=getpost();
        if (post!=null) {
            html = post.getAVFile("html");
            Url = html.getUrl();
            return Url;
        }else{
            return "about：blank";
        }
    }

  public AVObject getpost() {
      AVObject post=null;
      AVQuery query = new AVQuery("HTML");
      query = query.whereEqualTo("state", STATE_TRUE);
      try {
          s = query.count();
          if (s == 1) {
              post = query.getFirst();
              updatepost(post, "prent", STATE_FALSE);
          } else {
              post = query.whereEqualTo("prent", STATE_FALSE).getFirst();
              updatepost(post, "state", STATE_FALSE);
          }
      } catch (AVException e) {
      }
      return post;
  }
    public void updatepost(AVObject post, String name, Boolean value) {
        post.put(name, value);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i("LeanCloud", "Save successfully.");
                } else {
                    Log.e("LeanCloud", "Save failed.");
                }
            }
        });
    }
}

