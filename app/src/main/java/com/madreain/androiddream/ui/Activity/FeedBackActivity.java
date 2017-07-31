package com.madreain.androiddream.ui.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Manager.FeedBackManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.FeedBackModel;
import com.madreain.androiddream.views.LineEditText;
import com.madreain.androiddream.views.LoadingView;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户反馈
 */
public class FeedBackActivity extends AppCompatActivity {

    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;
    private LineEditText edt_feedback;
    private LineEditText edt_author;
    private TextView txt_send;
    FeedBackModel feedBackModel;
    String defaultFeedBack;

    //loading
    LoadingView loadingRoundView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initListener();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
        edt_feedback = (LineEditText) findViewById(R.id.edt_feedback);
        edt_author = (LineEditText) findViewById(R.id.edt_author);
        txt_send = (TextView) findViewById(R.id.txt_send);
        txt_title.setText(getResources().getString(R.string.txt_faq));
        img_add.setVisibility(View.GONE);
        feedBackModel = new FeedBackModel();
        defaultFeedBack = feedBackModel.toString();

    }

    private void initListener() {
        //返回
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange();
            }
        });
        //反馈内容
        edt_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                feedBackModel.setFeedbackContent(s.toString());
            }
        });
        // 反馈联系方式
        edt_author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                feedBackModel.setFeedbackContact(s.toString());
            }
        });
        //提交反馈
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(feedBackModel.getFeedbackContent())) {
                    if (!TextUtils.isEmpty(feedBackModel.getFeedbackContact())) {


                        loadingRoundView = new LoadingView(FeedBackActivity.this);
                        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
                        loadingRoundView.addPartentViewStartLoading(FeedBackActivity.this);

                        FeedBackManager.getInstance().addFeedBack(feedBackModel, new MBCallback.MBDataCallback() {
                            @Override
                            public void onSuccess() {
                                loadingRoundView.setSuccess();
                                Toast.makeText(FeedBackActivity.this, "意见反馈提交成功，感谢你的反馈", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(String code) {
                                loadingRoundView.setError();
                                Toast.makeText(FeedBackActivity.this, "意见反馈提交失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    } else {
                        Toast.makeText(FeedBackActivity.this, "反馈人联系方式不能为空，请输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FeedBackActivity.this, "意见反馈内容不能为空，请输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //判断是否发生改变
    private void isChange() {
        if (feedBackModel.toString().equals(defaultFeedBack)) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FeedBackActivity.this);
            builder.setTitle("提示");
            builder.setMessage("退出此次意见反馈编辑?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isChange();
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
