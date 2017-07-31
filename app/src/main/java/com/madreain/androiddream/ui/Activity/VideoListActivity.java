package com.madreain.androiddream.ui.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.util.Pair;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Constants;
import com.madreain.androiddream.core.Manager.VideoManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.SelfStudyModel;
import com.madreain.androiddream.core.Model.VideoKnowledge;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrClassicFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrDefaultHandler;
import com.madreain.androiddream.library.CommonPullToRefresh.PtrFrameLayout;
import com.madreain.androiddream.library.CommonPullToRefresh.loadmore.OnLoadMoreListener;
import com.madreain.androiddream.views.LoadingView;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;
    private ListView list_item;
    private PtrClassicFrameLayout ptrClassicFrameLayout;

    SelfStudyModel selfStudyModel;
    List<VideoKnowledge> videoKnowledgeList;

    VideoListAdapter videoListAdapter;

    //loading
    LoadingView loadingRoundView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_return = (ImageView) findViewById(R.id.img_return);
        img_add = (ImageView) findViewById(R.id.img_add);
        img_add.setVisibility(View.GONE);
        list_item = (ListView) findViewById(R.id.list_item);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout);
        //设置能加载更多
        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
            ptrClassicFrameLayout.setLoadMoreEnable(true);
        }

        videoKnowledgeList = new ArrayList<>();
        videoListAdapter = new VideoListAdapter();
        list_item.setAdapter(videoListAdapter);
    }

    private void initData() {
        selfStudyModel = (SelfStudyModel) getIntent().getSerializableExtra("SelfStudyModel");
        txt_title.setText(selfStudyModel.getTitle());
        refreshVideoList();
    }

    private void initListener() {
        //刷新方法
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshVideoList();
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
                        getMoreVideoList();
                    }
                }, 1);
            }
        });

        //点击进入详情界面
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(VideoListActivity.this, VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.TRANSITION, true);
                intent.putExtra("VideoKnowledge",videoKnowledgeList.get(i));
//                startActivity(intent);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Pair pair = new Pair<>(list_item, VideoPlayerActivity.IMG_TRANSITION);
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            VideoListActivity.this, pair);
                    ActivityCompat.startActivity(VideoListActivity.this, intent, activityOptions.toBundle());
                } else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }

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
                startActivity(new Intent(VideoListActivity.this, ShareKnowledgeActivity.class));
            }
        });
    }

    private void refreshVideoList() {
        loadingRoundView = new LoadingView(VideoListActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(VideoListActivity.this);


        VideoManager.getInstance().refreshVideoKnowledgeList(selfStudyModel.getSelfid(), new MBCallback.MBValueCallBack<List<VideoKnowledge>>() {
            @Override
            public void onSuccess(List<VideoKnowledge> result) {
                videoKnowledgeList = result;
                videoListAdapter.notifyDataSetChanged();
                ptrClassicFrameLayout.refreshComplete();
                loadingRoundView.setSuccess();
            }

            @Override
            public void onError(String code) {
                ptrClassicFrameLayout.refreshComplete();
                loadingRoundView.setError();
                Toast.makeText(VideoListActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void getMoreVideoList() {
        loadingRoundView = new LoadingView(VideoListActivity.this);
        loadingRoundView.setBackgroudColor(Color.parseColor("#66000000"));
        loadingRoundView.addPartentViewStartLoading(VideoListActivity.this);

        VideoManager.getInstance().getMoreVideoKnowledgeList(videoKnowledgeList.size(), selfStudyModel.getSelfid(), new MBCallback.MBValueCallBack<List<VideoKnowledge>>() {
            @Override
            public void onSuccess(List<VideoKnowledge> result) {
                videoKnowledgeList.addAll(result);
                videoListAdapter.notifyDataSetChanged();
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
                Toast.makeText(VideoListActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }


    private class VideoListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (videoKnowledgeList == null || videoKnowledgeList.size() == 0) {
                return 0;
            }
            return videoKnowledgeList.size();
        }

        @Override
        public VideoKnowledge getItem(int i) {
            return videoKnowledgeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TypeViewHolder typeViewHolder;
            if (view == null) {
                view = LayoutInflater.from(VideoListActivity.this).inflate(R.layout.item_type, viewGroup, false);
                typeViewHolder = new TypeViewHolder(view);
                view.setTag(typeViewHolder);
            } else {
                typeViewHolder = (TypeViewHolder) view.getTag();
            }
            VideoKnowledge videoKnowledge = videoKnowledgeList.get(i);
            //设置数据
            typeViewHolder.text.setText(videoKnowledge.getTitle());

            return view;
        }

        private class TypeViewHolder {
            private TextView text;

            private TypeViewHolder(View rootView) {
                this.text = (TextView) rootView.findViewById(R.id.text);
            }
        }
    }

}
