package com.demo.simple.sss;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.demo.simple.sss.com.demo.simple.sss.functions.*;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;


public class MainActivity extends AppCompatActivity {
    public Handler newsHandler = null;
    public Handler usermsgHandler=null;
    public String Url = "https://html5media.info/";
    public URL url;
    public String urlstr;
    private JSnews js;
    private Handler mHandler = new Handler();
    private GoogleApiClient client;
    public WebView newsWebview;
    static String TAG = "mainactivity！";
    EditText mUserName;
    EditText mpassword;
    EditText mAddress;
    EditText mWord;
    EditText mPone;
    AVUser currentUser ;
    AVUser upDateAVUser;
    TextView name;
    TextView word;
    JSONObject USER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        newsWebview = (WebView) findViewById(R.id.newswebview);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button loginbtn = (Button) findViewById(R.id.loginButton);
        Button callbackbtn = (Button) findViewById(R.id.callbackButton);
        Button setbtn=(Button)findViewById(R.id.setButton);
        name=(TextView) findViewById(R.id.name);
        word=(TextView)findViewById(R.id.word);
        //绑定leancloud数据库
        AVOSCloud.initialize(this, "zyY2iIQTX8otX65N9AST7YU5-gzGzoHsz", "UNXFoqqW2cvrdr06dnApYMTY");
        //更新webview
        loadwebview();

        currentUser=AVUser.getCurrentUser();
        if (currentUser != null) {
            // 允许用户使用应用
            loginbtn.setVisibility(View.GONE);
            usermsgHandler =new Handler(){
                /**
                 * Subclasses must implement this to receive messages.
                 *
                 * @param msg
                 */
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bundle usermsgbundle = msg.getData();
                    name.setText(usermsgbundle.get("name").toString());
                    word.setText(usermsgbundle.get("word").toString());
                    Log.d(TAG,"change msg");
                   // String newsHtmlurl = newsbundle.getString("newsHtmlurl");
                }
            };
            new Thread(){
                /**
                 * Calls the <code>run()</code> method of the Runnable object the receiver
                 * holds. If no Runnable is set, does nothing.
                 *
                 * @see Thread#start
                 */
                @Override
                public void run() {
                    Message usermsg = newsHandler.obtainMessage();
                    user user=new user();
                    upDateAVUser=user.getuser(currentUser);
                    Bundle usermsgbundle = new Bundle();
                    if (upDateAVUser!=null) {
                        //是否需要呢？
                        /*try {
                            USER.put("name", upDateAVUser.getUsername().toString());
                            USER.put("word", upDateAVUser.get("word").toString());
                            USER.put("address", upDateAVUser.get("address").toString());
                            USER.put("phone",upDateAVUser.getMobilePhoneNumber().toString());

                        }catch (JSONException e){

                        }*/
                        usermsgbundle.putString("name",upDateAVUser.get("username").toString());
                        usermsgbundle.putString("word",upDateAVUser.get("word").toString());
                        Log.d(TAG,"find msg");
                    }else{

                    }
                    usermsg.setData(usermsgbundle);
                    usermsgHandler.sendMessage(usermsg);
                    // user.updateuser(upDateAVUser);
                }
            }.start();
        } else {
           //缓存用户对象为空时， 可打开用户注册界面…显示loginbutton
            setbtn.setVisibility(View.GONE);
        }
        //登录
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "这里应该做成登录过的修改，未登录的注册，而且界面应该相同" +
                        "");
                if (currentUser != null) {
                    Log.i(TAG, "已经登录，去设置其他信息");
                    // 允许用户使用应用
                } else {
                    LoginDialog(); //缓存用户对象为空时， 可打开用户注册界面…
                }
            }
        });
        //反馈
        callbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里的代码简单，但不好用
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("请输入").
                        setIcon(android.R.drawable.ic_dialog_info).
                        setView(new EditText(MainActivity.this)).
                        setPositiveButton("确定",null).
                        setNegativeButton("取消", null).show();
            }
        });
        //设置个人信息
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置头像，地址，个性标语
                //头像直接点击头像设置
                //只要set button出现，currentUser！=null，所以可以直接使用
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View setview=layoutInflater.inflate(R.layout.setusermsglayout,null);

                mUserName = (EditText) setview.findViewById(R.id.edit_username);
                mWord=(EditText)setview.findViewById(R.id.edit_word);
                mPone=(EditText)setview.findViewById(R.id.edit_phone);
                mAddress=(EditText)setview.findViewById(R.id.edit_address);
                mUserName.setText(upDateAVUser.getUsername());
                mWord.setText(upDateAVUser.get("word").toString());
                mPone.setText(upDateAVUser.getMobilePhoneNumber());
                mAddress.setText(upDateAVUser.get("address").toString());

                builder.setTitle("个人信息").setView(setview).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //更改信息
                        if(upDateAVUser!=null) {
                            upDateAVUser.setUsername(mUserName.getText().toString());
                            upDateAVUser.setMobilePhoneNumber(mPone.getText().toString());
                            upDateAVUser.put("address", mAddress.getText().toString());
                            upDateAVUser.put("word", mWord.getText().toString());
                            upDateAVUser.saveInBackground();
                            name.setText(upDateAVUser.getUsername().toString());
                            word.setText(upDateAVUser.get("word").toString());
                        }else{
                            Log.d(TAG,TAG);
                        }
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //放弃操作
                    }
                }).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Log.i(TAG, "2");
                /*//新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(MainActivity.this, Community.class);
                startActivity(intent);*/


                //android自带的分享功能
                /*Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                String describe="看一看，瞧一瞧\n"+url;
                intent.putExtra(Intent.EXTRA_TEXT, describe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));*/
            }
        });
    }

    public void LoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View diolog=layoutInflater.inflate(R.layout.loginlayout,null);
        //这两个应改定义到外面
        mUserName = (EditText) diolog.findViewById(R.id.edit_username);
        mpassword = (EditText) diolog.findViewById(R.id.edit_password);
        builder.setTitle("注册").setView(diolog).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AVUser user = new AVUser();
                user.setUsername(mUserName.getText().toString());
                user.setPassword(mpassword.getText().toString());
                //user.setEmail("hang@leancloud.rocks");
                // 其他属性可以像其他AVObject对象一样使用put方法添加
                //user.put("phone", "186-1234-0000");
                Log.i("hheh","hhhhhhhhhhhhhhhhhhhhhhhhh");
                user.signUpInBackground(new SignUpCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            Log.i(TAG, "denglu successfully");// successfully
                        } else {
                            Log.i(TAG, "denglu failed");
                        }
                    }
                });
            }
        }).create().show();
    }


    private void loadwebview() {
        //内容的渲染需要webviewChromClient去实现，设置webviewChromClient基类，解决js中alert不弹出的问题和其他内容渲染问题
        //这里解决不跳转到系统刘篮球的问题
        newsWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        //设置参数
        // newsWebview.getSettings().setBuiltInZoomControls(true);
        newsWebview.getSettings().setJavaScriptEnabled(true);
        //把js绑定到全局的myjs上，myjs的作用域是全局的，初始化后可随处使用
        //newsWebview.addJavascriptInterface(js, "myjs");
        newsHandler = new Handler() {
            /**
             * Subclasses must implement this to receive messages.
             *
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle newsbundle = msg.getData();
                String newsHtmlurl = newsbundle.getString("newsHtmlurl");
                newsWebview.loadUrl(newsHtmlurl);
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message newmessage = newsHandler.obtainMessage();
                Bundle newsbundle = new Bundle();
                getUrl getUrl = new getUrl();
                //得到服务器上的html文件
                urlstr = getUrl.getUrl();
                newsbundle.putString("newsHtmlurl", urlstr);
                //得到本地的HTML文件
                //newsbundle.putString("newsHtmlurl","file:///android_asset/index2.html");
                newmessage.setData(newsbundle);
                newsHandler.sendMessage(newmessage);
            }
        }.start();
        //实例化js对象
        js = new JSnews(this);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}
