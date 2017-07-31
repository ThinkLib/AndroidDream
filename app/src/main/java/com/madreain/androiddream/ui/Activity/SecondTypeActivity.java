package com.madreain.androiddream.ui.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.SecondTypeManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MSecondType;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrClassicFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrDefaultHandler;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.loadmore.OnLoadMoreListener;
import com.madreain.androiddream.utils.NetworkUtils;
import com.madreain.androiddream.views.LoadingView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 大分类下对应小分类
 *
 * @author madreain
 */
public class SecondTypeActivity extends AppCompatActivity {

    private ListView list_item;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    int mid = -1;
    List<MSecondType> mSecondTypeList;
    SecondTypeAdapter secondTypeAdapter;
    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;

    //loading
    LoadingView loadingRoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_type);
        initView();
//        addData();
        initData();
        initListener();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
        list_item = (ListView) findViewById(R.id.list_item);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout);
        //设置能加载更多
        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
            ptrClassicFrameLayout.setLoadMoreEnable(true);
        }
        mSecondTypeList = new ArrayList<>();
        secondTypeAdapter = new SecondTypeAdapter();
        list_item.setAdapter(secondTypeAdapter);

    }

    private void initData() {
        MType mType = (MType) getIntent().getSerializableExtra("mType");
        if (mType != null) {
            mid = mType.getMid();
            //判断网络如果有网络选择网络刷新
            if(NetworkUtils.isNetworkAvailable(SecondTypeActivity.this)){
                refreshSencondType();
            }else {
                setLocalData();
            }

            //设置标题
            txt_title.setText(mType.getTitle());
        }
    }

    //本地数据的设置
    private void setLocalData() {
        mSecondTypeList = SecondTypeManager.getInstance().getMSecondType();
        secondTypeAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        //刷新方法
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshSencondType();
                    }
                }, 200);
            }
        });

        //加载更多
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMoreSencondType();
                    }
                }, 1);
            }
        });

        //点击进入详情界面
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MSecondType mSecondType = mSecondTypeList.get(i);
                Intent intent = new Intent(SecondTypeActivity.this, Type2DetailsActivity.class);
                intent.putExtra("mSecondType", mSecondType);
                startActivity(intent);
            }
        });
        //返回
        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //增加一个提交推荐内容给掘梦
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondTypeActivity.this, ShareKnowledgeActivity.class));
            }
        });
    }


    private void refreshSencondType() {

        loadingRoundView = new LoadingView(SecondTypeActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(SecondTypeActivity.this);

        SecondTypeManager.getInstance().refreshSecondTypeByMid(mid, new MBCallback.MBValueCallBack<List<MSecondType>>() {
            @Override
            public void onSuccess(List<MSecondType> result) {
                mSecondTypeList = result;
                secondTypeAdapter.notifyDataSetChanged();
                ptrClassicFrameLayout.refreshComplete();

                loadingRoundView.setSuccess();
            }

            @Override
            public void onError(String code) {
                ptrClassicFrameLayout.refreshComplete();
                loadingRoundView.setError();
                Toast.makeText(SecondTypeActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getMoreSencondType() {
        loadingRoundView = new LoadingView(SecondTypeActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(SecondTypeActivity.this);

        SecondTypeManager.getInstance().getMoreSecondTypeByMid(mSecondTypeList.size(), mid, new MBCallback.MBValueCallBack<List<MSecondType>>() {
            @Override
            public void onSuccess(List<MSecondType> result) {
                mSecondTypeList.addAll(result);
                secondTypeAdapter.notifyDataSetChanged();
                if (result != null && result.size() > 0) {
                    ptrClassicFrameLayout.loadMoreComplete(true);
                } else {
                    //加载更多时，没有数据返回时的设置已经到底了
                    ptrClassicFrameLayout.setLodeMoreFinish();
                }
                loadingRoundView.setSuccess();
            }

            @Override
            public void onError(String code) {
                if (code.equals(Constants.Error)) {
                    ptrClassicFrameLayout.setNetworkAnomalyFinish();
                } else {
                    ptrClassicFrameLayout.loadMoreComplete(true);
                }
                loadingRoundView.setError();
                Toast.makeText(SecondTypeActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }


    private class SecondTypeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mSecondTypeList == null || mSecondTypeList.size() == 0) {
                return 0;
            }
            return mSecondTypeList.size();
        }

        @Override
        public MSecondType getItem(int i) {
            return mSecondTypeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TypeViewHolder typeViewHolder;
            if (view == null) {
                view = LayoutInflater.from(SecondTypeActivity.this).inflate(R.layout.item_type, viewGroup, false);
                typeViewHolder = new TypeViewHolder(view);
                view.setTag(typeViewHolder);
            } else {
                typeViewHolder = (TypeViewHolder) view.getTag();
            }
            MSecondType mSecondType = mSecondTypeList.get(i);
            //设置数据
            typeViewHolder.text.setText(mSecondType.getTitle());
//            typeViewHolder.text.setTextSize(PixelOrdpManager.sp2px(SecondTypeActivity.this, 12));

//            //适配
//            int width = WindowUtils.getWindowWidth(SecondTypeActivity.this);
//            int setwidth = (width - PixelOrdpManager.dip2px(SecondTypeActivity.this, 3)) / 2;
//            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(setwidth, setwidth);
//            view.setLayoutParams(layoutParams);
            return view;
        }

        private class TypeViewHolder {
            private View rootView;
            private TextView text;

            private TypeViewHolder(View rootView) {
                this.rootView = rootView;
                this.text = (TextView) rootView.findViewById(R.id.text);
            }
        }
    }


    //大分类下添加小分类
    private void addData() {
        List<MSecondType> mSecondTypeList = new ArrayList<>();
        //java mid=0
        mSecondTypeList.add(new MSecondType(0, 0, "java语法基础"));
        mSecondTypeList.add(new MSecondType(0, 1, "面向对象"));
        mSecondTypeList.add(new MSecondType(0, 2, "面向接口编程"));
        //Android基础  mid=1
        mSecondTypeList.add(new MSecondType(1, 3, "开发环境"));
        mSecondTypeList.add(new MSecondType(1, 4, "基础知识"));
        mSecondTypeList.add(new MSecondType(1, 5, "四大组件"));
        mSecondTypeList.add(new MSecondType(1, 6, "常用控件/用户交互"));
        mSecondTypeList.add(new MSecondType(1, 7, "网络访问"));
        mSecondTypeList.add(new MSecondType(1, 8, "图片加载"));
        mSecondTypeList.add(new MSecondType(1, 9, "数据存储"));
        mSecondTypeList.add(new MSecondType(1, 10, "动画效果"));
        mSecondTypeList.add(new MSecondType(1, 11, "自定义控件"));
        mSecondTypeList.add(new MSecondType(1, 12, "多媒体技术/资源文件/硬件模块"));
        mSecondTypeList.add(new MSecondType(1, 13, "JNI"));
        mSecondTypeList.add(new MSecondType(1, 14, "常用第三方库/常用第三方sdk"));
        mSecondTypeList.add(new MSecondType(1, 15, "常用工具类"));
        mSecondTypeList.add(new MSecondType(1, 16, "APP优化"));
        //项目常用框架 mid=2
        mSecondTypeList.add(new MSecondType(2, 17, "网络请求"));
        mSecondTypeList.add(new MSecondType(2, 18, "图片加载"));
        mSecondTypeList.add(new MSecondType(2, 19, "数据库"));
        mSecondTypeList.add(new MSecondType(2, 20, "数据解析"));
        mSecondTypeList.add(new MSecondType(2, 21, "下拉刷新"));
        mSecondTypeList.add(new MSecondType(2, 22, "导航栏"));
        mSecondTypeList.add(new MSecondType(2, 23, "对话框"));
        mSecondTypeList.add(new MSecondType(2, 24, "依赖注入"));
        mSecondTypeList.add(new MSecondType(2, 25, "事件传递"));
        mSecondTypeList.add(new MSecondType(2, 26, "日志框架"));
        mSecondTypeList.add(new MSecondType(2, 27, "指示器"));
        mSecondTypeList.add(new MSecondType(2, 28, "照片选择器"));
        mSecondTypeList.add(new MSecondType(2, 29, "照片阅览器"));
        mSecondTypeList.add(new MSecondType(2, 30, "BAT公司各种高仿效果"));
        //热门/新技术 mid=3
        mSecondTypeList.add(new MSecondType(3, 31, "测试"));
        mSecondTypeList.add(new MSecondType(3, 32, "混淆"));
        mSecondTypeList.add(new MSecondType(3, 33, "反编译"));
        mSecondTypeList.add(new MSecondType(3, 34, "studio插件"));
        mSecondTypeList.add(new MSecondType(3, 35, "项目架构"));
        mSecondTypeList.add(new MSecondType(3, 36, "响应式编程"));
        mSecondTypeList.add(new MSecondType(3, 37, "github"));
        mSecondTypeList.add(new MSecondType(3, 38, "webview/webp"));
        mSecondTypeList.add(new MSecondType(3, 39, "RecyclerView"));
        mSecondTypeList.add(new MSecondType(3, 40, "React Native"));
        mSecondTypeList.add(new MSecondType(3, 41, "OpenGL ES"));
        mSecondTypeList.add(new MSecondType(3, 42, "热修复"));
        mSecondTypeList.add(new MSecondType(3, 43, "推送"));
        mSecondTypeList.add(new MSecondType(3, 44, "IM"));
        mSecondTypeList.add(new MSecondType(3, 45, "组件化"));
        mSecondTypeList.add(new MSecondType(3, 46, "沉浸式"));
        mSecondTypeList.add(new MSecondType(3, 47, "AccessibilityService"));
        mSecondTypeList.add(new MSecondType(3, 48, "数据打点"));
        mSecondTypeList.add(new MSecondType(3, 49, "异常"));

        mSecondTypeList.add(new MSecondType(3, 50, "Crash处理"));
        mSecondTypeList.add(new MSecondType(3, 51, "6.0权限"));
        mSecondTypeList.add(new MSecondType(3, 52, "Android N 分屏"));
        //开源项目 mid=4
        //面试 mid=5

        SecondTypeManager.getInstance().addSecondType(mSecondTypeList, new MBCallback.MBDataCallback() {
            @Override
            public void onSuccess() {
                Log.d("SecondTypeActivity", "成功");
            }

            @Override
            public void onError(String code) {
                Log.d("SecondTypeActivity", "失败");
            }

            @Override
            public void onFinished() {

            }
        });

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
