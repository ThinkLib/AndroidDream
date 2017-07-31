package com.madreain.androiddream.ui.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.KnowledgeManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MTKnowledge;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrClassicFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrDefaultHandler;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.loadmore.OnLoadMoreListener;
import com.madreain.androiddream.views.LoadingView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author madreain
 * @time 2017/4/5
 * @deprecated 开源项目/面试大分类下对应的相关文章
 */
public class TypeDetailsActivity extends AppCompatActivity {

    private ListView list_item;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    int mid = -1;
    List<MTKnowledge> mtKnowledgeList;
    KnowledgeAdapter knowledgeAdapter;
    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;

    //loading
    LoadingView loadingRoundView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_details);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        list_item = (ListView) findViewById(R.id.list_item);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout);
        //设置能加载更多
        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
            ptrClassicFrameLayout.setLoadMoreEnable(true);
        }
        mtKnowledgeList = new ArrayList<>();
        knowledgeAdapter = new KnowledgeAdapter();
        list_item.setAdapter(knowledgeAdapter);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
    }

    private void initData() {
        MType mType = (MType) getIntent().getSerializableExtra("mType");
        if (mType != null) {
            mid = mType.getMid();
            refreshMTKnowledge();
            //设置标题
            txt_title.setText(mType.getTitle());
        }
    }

    private void initListener() {
        //刷新方法
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshMTKnowledge();
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
                        getMoreMTKnowledge();
                    }
                }, 1);
            }
        });
        //点击进入详情页
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TypeDetailsActivity.this, WebActivity.class);
                intent.putExtra("MTKnowledge", mtKnowledgeList.get(i));
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
        // 增加一个提交推荐内容给掘梦
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TypeDetailsActivity.this, ShareKnowledgeActivity.class));
            }
        });
    }

    private void refreshMTKnowledge() {

        loadingRoundView = new LoadingView(TypeDetailsActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(TypeDetailsActivity.this);


        KnowledgeManager.getInstance().refreshKnowledgeByMid(mid, new MBCallback.MBValueCallBack<List<MTKnowledge>>() {
            @Override
            public void onSuccess(List<MTKnowledge> result) {
                mtKnowledgeList = result;
                knowledgeAdapter.notifyDataSetChanged();
                ptrClassicFrameLayout.refreshComplete();

                loadingRoundView.setSuccess();
            }

            @Override
            public void onError(String code) {
                ptrClassicFrameLayout.refreshComplete();
                loadingRoundView.setError();
                Toast.makeText(TypeDetailsActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getMoreMTKnowledge() {
        loadingRoundView = new LoadingView(TypeDetailsActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(TypeDetailsActivity.this);


        KnowledgeManager.getInstance().getMoreKnowledgeByMid(mtKnowledgeList.size(), mid, new MBCallback.MBValueCallBack<List<MTKnowledge>>() {
            @Override
            public void onSuccess(List<MTKnowledge> result) {
                mtKnowledgeList.addAll(result);
                knowledgeAdapter.notifyDataSetChanged();
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
                } else
                    ptrClassicFrameLayout.loadMoreComplete(true);
                loadingRoundView.setError();
                Toast.makeText(TypeDetailsActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private class KnowledgeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mtKnowledgeList == null || mtKnowledgeList.size() == 0) {
                return 0;
            }
            return mtKnowledgeList.size();
        }

        @Override
        public MTKnowledge getItem(int i) {
            return mtKnowledgeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            KnowledgeViewHolder knowledgeViewHolder;
            if (view == null) {
                view = LayoutInflater.from(TypeDetailsActivity.this).inflate(R.layout.item_knowledge, viewGroup, false);
                knowledgeViewHolder = new KnowledgeViewHolder(view);
                view.setTag(knowledgeViewHolder);
            } else {
                knowledgeViewHolder = (KnowledgeViewHolder) view.getTag();
            }
            //设置数据
            MTKnowledge mtKnowledge = mtKnowledgeList.get(i);
            //标题
            knowledgeViewHolder.text_title.setText(mtKnowledge.getTitle());
            //项目描述
            StringBuilder stringBuilderdesc = new StringBuilder("描述：");
            stringBuilderdesc.append(mtKnowledge.getDesc());
            knowledgeViewHolder.text_desc.setText(stringBuilderdesc);
            //作者
            StringBuilder stringBuilderauthor = new StringBuilder("作者：");
            stringBuilderauthor.append(mtKnowledge.getAuthor());
            knowledgeViewHolder.text_author.setText(stringBuilderauthor);
            //来源
            StringBuilder stringBuildersorce = new StringBuilder("来源：");
            stringBuildersorce.append(mtKnowledge.getSorce());
            knowledgeViewHolder.text_sorce.setText(stringBuildersorce);
            return view;
        }

        private class KnowledgeViewHolder {
            private View rootView;
            private TextView text_title;
            private TextView text_desc;
            private TextView text_author;
            private TextView text_sorce;
            private RelativeLayout layout;

            private KnowledgeViewHolder(View rootView) {
                this.rootView = rootView;
                this.text_title = (TextView) rootView.findViewById(R.id.text_title);
                this.text_desc = (TextView) rootView.findViewById(R.id.text_desc);
                this.text_author = (TextView) rootView.findViewById(R.id.text_author);
                this.text_sorce = (TextView) rootView.findViewById(R.id.text_sorce);
                this.layout = (RelativeLayout) rootView.findViewById(R.id.layout);
            }

        }
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
