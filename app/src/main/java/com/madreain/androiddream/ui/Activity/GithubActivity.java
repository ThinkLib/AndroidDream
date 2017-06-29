package com.madreain.androiddream.ui.Activity;

import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.utils.UmengUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class GithubActivity extends AppCompatActivity {

    private TextView tv_title;
    private ImageView img_return;
    private ImageView img_share;
    private WebView webview;
    private ProgressBar pb_content;
    private ImageView img_delete;
    String github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
        initListener();

    }


    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_share = (ImageView) findViewById(R.id.img_share);
        webview = (WebView) findViewById(R.id.webview);
        initWebView();
        pb_content = (ProgressBar) findViewById(R.id.pb_content);
        img_delete = (ImageView) findViewById(R.id.img_delete);

    }

    //    设置WebView 缓存模式
    private void initWebView() {
        webview.getSettings().setJavaScriptEnabled(true);

        //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为 https与http
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
//        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = Environment.getExternalStorageDirectory() + "/Madreain";
        //设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);

        webview.getSettings().setUserAgentString(webview.getSettings().getUserAgentString() + Constants.MADREAIN_UA_EXTRA);
    }

    private void initData() {
        github = getIntent().getStringExtra("Github");
        webview.loadUrl(github);
//        WebSettings settings = wv_content.getSettings();
//        settings.setJavaScriptEnabled(true);


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {

            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pb_content.setProgress(progress);
                if (progress == 100) {
                    pb_content.setVisibility(View.GONE);
                } else {
                    pb_content.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    tv_title.setText(title);
                }
            }
        });
    }

    private void initListener() {
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webview.canGoBack()) {
                    webview.goBack();
                    img_delete.setVisibility(View.GONE);
                } else {
                    finish();
                }
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (github != null) {
                    UMWeb web = new UMWeb(github);
                    web.setTitle("掘梦");//标题
                    web.setThumb(new UMImage(GithubActivity.this,Constants.Madreain_icon));  //缩略图
                    web.setDescription("下载掘梦，了解更多Android前端知识");//描述
                    UmengUtil.share(GithubActivity.this, web);
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                img_delete.setVisibility(View.GONE);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
