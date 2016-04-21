package com.example.wq.chatrobot;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.wq.help.javaJson;
import com.example.wq.newclass.RecoResult;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.util.Log;
import android.widget.LinearLayout;


import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener{

    private final String baseurl="http://apiaiservice.duapp.com/chat";
    private LinearLayout sentdiv;
    private RecognizerDialog myDialog;//科大讯飞语音输入dialog
    private WebView webView;    //网页容器
    private EditText sendInput;    //输入框
    private static final String APPID = "=56fba72d";    //科大讯飞appid
    private String RecoString="";//存储科大讯飞返回的语句；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 重写返回按钮功能
     */
    public void onBackPressed() {

       if(webView.canGoBack()){
           webView.goBack();
           Log.i("inf", "goback:"+webView.getUrl());
       }else{
           MainActivity.this.finish();
       }
    }

    /**
     * 初始化
     */
    private void init()
    {
        //控件绑定
        sentdiv=(LinearLayout)findViewById(R.id.sentdiv);
        findViewById(R.id.startRecognizer).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        sendInput=(EditText)findViewById(R.id.text);
        webView=((WebView) findViewById(R.id.content));
        //设置webView的javascript可用，需要让webView调用javascript
        WebSettings setting=webView.getSettings();
        setting.setJavaScriptEnabled(true);
        //科大讯飞语音识别SpeechUtility，用来记录Appid
        SpeechUtility.createUtility(this, SpeechConstant.APPID+APPID);
        //防止跳转到系统默认浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //页面跳转后设置输入框不可见,而且不占空间
                MainActivity.this.sentdiv.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //如果是首页，就让输入框显示
                if(url.equals(baseurl)){
                   // Log.i("inf","finish:"+url);
                    sentdiv.setVisibility(View.VISIBLE);
                }
            }
        });
        //加载聊天界面
        webView.loadUrl(baseurl);
    }

    /**
     * 按钮监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.startRecognizer:
                myRecognize();
                break;
            case R.id.send:
                myJavascript(sendInput.getText().toString());
                break;
            default :
                break;

        }
    }

    //java调用webView中的js脚本函数
    private void myJavascript(String send)
    {
        if(sendInput.getText()!=null)
        {
            //javascript中的方法定义为：function ajaxRequest(send){}
            String call = "javascript:ajaxRequest(\""+send+"\")";
            webView.loadUrl(call);
            //重置输入框
            sendInput.setText(null);
        }
    }

    private void myRecognize()
    {
        //重置语音识别的语句，以用来存储下一次的结果
        RecoString="";
        myDialog =new RecognizerDialog(this,mInitListener);
        //设置引擎为转写
        myDialog.setParameter(SpeechConstant.DOMAIN, "it");
        //设置识别语言为中文
        myDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置方言为普通话
        myDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //设置录音采样率为
        myDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        myDialog.setListener(recognizerDialogListener);
        //开始识别
        myDialog.show();
    }

    //创建科大讯飞语音监听器
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener(){

        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
            // TODO Auto-generated method stub
           try{
              // Log.i("Inf",recognizerResult.getResultString());
               RecoResult result=javaJson.getReconObject(recognizerResult.getResultString());
               RecoString+=result.toString();
               if(b==true)
               {
                   //去掉科大讯飞识别出的句子的最后一个标点符号
                   RecoString=RecoString.substring(0,RecoString.length()-1);
                   //Log.i("Inf", RecoString);
                   //调用javascript
                   myJavascript(RecoString);
               }
           }catch (Exception e)
           {
               Log.i("Inf",e.getMessage());
           }

        }
        @Override
        public void onError(SpeechError error) {
            // TODO Auto-generated method stub
        }

    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("LOGCAT", "初始化code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.e("LOGCAT","初始化失败，错误码：" + code);
            }
        }
    };

}

