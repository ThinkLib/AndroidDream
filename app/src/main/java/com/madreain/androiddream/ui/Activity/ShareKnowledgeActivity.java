package com.madreain.androiddream.ui.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Manager.ShareKnowledgeManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.ShareKnowledge;
import com.madreain.androiddream.library.kprogresshud.KProgressHUD;
import com.madreain.androiddream.utils.MUtil;
import com.madreain.androiddream.views.LineEditText;
import com.umeng.analytics.MobclickAgent;

/**
 * 分享知识给大家共同查看
 */
public class ShareKnowledgeActivity extends AppCompatActivity {

    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;
    private LineEditText edt_title;
    private LineEditText edt_desc;
    private LineEditText edt_author;
    private LineEditText edt_sorce;
    private LineEditText edt_url;
    private LineEditText edt_email;
    private TextView txt_send;

    //提交文章
    ShareKnowledge shareKnowledge;
    String initialString;

    //loading
    KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_knowledge);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
        edt_title = (LineEditText) findViewById(R.id.edt_title);
        edt_desc = (LineEditText) findViewById(R.id.edt_desc);
        edt_author = (LineEditText) findViewById(R.id.edt_author);
        edt_sorce = (LineEditText) findViewById(R.id.edt_sorce);
        edt_url = (LineEditText) findViewById(R.id.edt_url);
        edt_email = (LineEditText) findViewById(R.id.edt_email);
        txt_send = (TextView) findViewById(R.id.txt_send);

        shareKnowledge = new ShareKnowledge();
        initialString = shareKnowledge.toString();
    }

    private void initData() {
        txt_title.setText("提交Android相关文章给掘梦");
        img_add.setVisibility(View.GONE);

    }

    private void initListener() {
        //返回
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange();
            }
        });
        //文章标题
        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setTitle(s.toString());
            }
        });
        // 文章相关描述
        edt_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setDesc(s.toString());
            }
        });
        //  文章作者
        edt_author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setAuthor(s.toString());
            }
        });
        //  文章来源
        edt_sorce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setSorce(s.toString());
            }
        });
        //   文章链接
        edt_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setUrl(s.toString());
            }
        });
        //  邮箱
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                shareKnowledge.setEmail(s.toString());
            }
        });
        //提交文章
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文章标题
                if (!TextUtils.isEmpty(shareKnowledge.getTitle())) {
                    // 项目描述
                    if (!TextUtils.isEmpty(shareKnowledge.getDesc())) {
                        // 作者
                        if (!TextUtils.isEmpty(shareKnowledge.getAuthor())) {
                            // 来源
                            if (!TextUtils.isEmpty(shareKnowledge.getSorce())) {
                                // 文章的链接
                                if (!TextUtils.isEmpty(shareKnowledge.getUrl())) {
                                    if (Patterns.WEB_URL.matcher(shareKnowledge.getUrl()).matches()) {
                                        // 邮箱
                                        if (!TextUtils.isEmpty(shareKnowledge.getEmail())) {
                                            if (MUtil.isEmail(shareKnowledge.getEmail())) {

                                                kProgressHUD = KProgressHUD.create(ShareKnowledgeActivity.this)
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setCancellable(true)
                                                        .setAnimationSpeed(1)
                                                        .setDimAmount(0.5f)
                                                        .show();


                                                //提交文章的接口
                                                ShareKnowledgeManager.getInstance().addShareKnowledge(shareKnowledge, new MBCallback.MBDataCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        kProgressHUD.dismiss();
                                                        Toast.makeText(ShareKnowledgeActivity.this, "文章提交成功，感谢你的共享", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onError(String code) {
                                                        kProgressHUD.dismiss();
                                                        Toast.makeText(ShareKnowledgeActivity.this, "文章提交失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFinished() {

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(ShareKnowledgeActivity.this, "文章邮箱格式有误，请重新输入", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(ShareKnowledgeActivity.this, "文章邮箱不能为空，请输入", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ShareKnowledgeActivity.this, "文章链接无效，请重新输入", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ShareKnowledgeActivity.this, "文章链接不能为空，请输入", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ShareKnowledgeActivity.this, "文章来源不能为空，请输入", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ShareKnowledgeActivity.this, "文章作者不能为空，请输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ShareKnowledgeActivity.this, "文章相关描述不能为空，请输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShareKnowledgeActivity.this, "文章标题不能为空，请输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //判断是否发生改变
    private void isChange() {
        if (shareKnowledge.toString().equals(initialString)) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShareKnowledgeActivity.this);
            builder.setTitle("提示");
            builder.setMessage("退出此次编辑?");
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
