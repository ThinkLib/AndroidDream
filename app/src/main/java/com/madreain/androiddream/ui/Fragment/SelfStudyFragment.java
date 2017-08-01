package com.madreain.androiddream.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Manager.SelfStudyManager;
import com.madreain.androiddream.core.Manager.VideoManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.SelfStudyModel;
import com.madreain.androiddream.library.wenchao.cardstack.CardStack;
import com.madreain.androiddream.ui.Activity.ShareKnowledgeActivity;
import com.madreain.androiddream.ui.Activity.VideoListActivity;
import com.madreain.androiddream.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 自学内容
 * https://github.com/Diolor/Swipecards
 * https://github.com/wenchaojiang/AndroidSwipeableCardStack
 *
 * @author madreain
 * @time 2017/4/5
 */
public class SelfStudyFragment extends Fragment {
    private View rootview = null;
    private Context mContext;
    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;


    private CardStack cardstack;
    private CardsDataAdapter mCardAdapter;
    ArrayList<SelfStudyModel> selfStudyModels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_selfstudy, null);
        mContext = getContext();
        initView(rootview);
//        addData();
        initData();
        initListener();
        return rootview;
    }

    private void addData() {
        VideoManager.getInstance().addVideoKnowledgeToServer(mContext);
    }


    private void initView(View rootview) {
        txt_title = (TextView) rootview.findViewById(R.id.txt_title);
        img_return = (ImageView) rootview.findViewById(R.id.img_return);
        img_add = (ImageView) rootview.findViewById(R.id.img_add);
        cardstack = (CardStack) rootview.findViewById(R.id.cardstack);
        txt_title.setText("自学");
        img_return.setVisibility(View.GONE);
        selfStudyModels = new ArrayList<>();

    }

    //卡片式布局展示设置数据
    private void initData() {
        cardstack.setContentResource(R.layout.card_content);
//        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(mContext);
//        selfStudyModels = (ArrayList<SelfStudyModel>) SelfStudyManager.getInstance().getDefaultSelfStudyModel();
//        cardstack.setAdapter(mCardAdapter);
//        refreshData();
        //判断网络如果有网络选择网络刷新
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            refreshData();
        }else {
            setLocalData();
        }

        //自定义显示滑动的监听方法
        cardstack.setListener(new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int section, float distance) {
                return (distance > 300) ? true : false;
            }

            @Override
            public boolean swipeStart(int section, float distance) {
                return true;
            }

            @Override
            public boolean swipeContinue(int section, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void discarded(int mIndex, int direction) {
                //当当前的显示的位置是倒数第一个是再次循环插入数据，便于查看
                if(mIndex==selfStudyModels.size()){
                    selfStudyModels.addAll(SelfStudyManager.getInstance().getDefaultSelfStudyModel());
                    cardstack.setAdapter(mCardAdapter);
                }
                //  右滑表示选择  左滑表示略过
//                Log.d("SelfStudyFragment", "mIndex:" + mIndex);
                if (direction == 2 || direction == 0) {
//                    Log.d("SelfStudyFragment", "左滑");
                } else if (direction == 3 || direction == 1) {
//                    Log.d("SelfStudyFragment", "右滑");
                    Intent intent=new Intent(mContext, VideoListActivity.class);
                    intent.putExtra("SelfStudyModel",selfStudyModels.get(mIndex-1));
                    startActivity(intent);

//                    Intent intent = new Intent(mContext, VideoPlayerActivity.class);
//                    intent.putExtra(VideoPlayerActivity.TRANSITION, true);
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        Pair pair = new Pair<>(cardstack, VideoPlayerActivity.IMG_TRANSITION);
//                        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                getActivity(), pair);
//                        ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
//                    } else {
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//                    }

                }

            }

            @Override
            public void topCardTapped() {

            }
        });


    }

    private void setLocalData() {
        selfStudyModels= (ArrayList<SelfStudyModel>) SelfStudyManager.getInstance().getSelfStudyModel();
        cardstack.setAdapter(mCardAdapter);
    }

    private void refreshData() {
        SelfStudyManager.getInstance().refreshSelfStudyModelList(new MBCallback.MBValueCallBack<List<SelfStudyModel>>() {
            @Override
            public void onSuccess(List<SelfStudyModel> result) {
                selfStudyModels= (ArrayList<SelfStudyModel>) result;
                cardstack.setAdapter(mCardAdapter);
            }

            @Override
            public void onError(String code) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    private void initListener() {
        // 增加一个提交推荐内容给掘梦
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ShareKnowledgeActivity.class));
            }
        });
    }


    public void refreshFragmentData() {

    }


    //卡片式布局的适配器
    public class CardsDataAdapter extends ArrayAdapter<SelfStudyModel> {


        public CardsDataAdapter(Context context) {
            super(context, R.layout.card_content);
        }

        @Override
        public int getCount() {
            if (selfStudyModels == null || selfStudyModels.size() == 0) {
                return 0;
            } else {
                return selfStudyModels.size();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CardsViewHolder cardsViewHolder = new CardsViewHolder(convertView);
            SelfStudyModel selfStudyModel = selfStudyModels.get(position);
            if (selfStudyModel != null) {
                if (selfStudyModel.getTitle() != null) {
                    cardsViewHolder.content.setText(selfStudyModel.getTitle());
                } else {
                    cardsViewHolder.content.setText("");
                }
                if (!TextUtils.isEmpty(selfStudyModel.getPic())) {
                    Glide.with(mContext).load(selfStudyModel.getPic()).into(cardsViewHolder.img_v);
                } else {
                    cardsViewHolder.img_v.setImageResource(R.mipmap.ic_launcher);
                }
            }

            return convertView;
        }


    }

    public static class CardsViewHolder {
        public ImageView img_v;
        public TextView content;

        public CardsViewHolder(View rootView) {
            this.img_v = (ImageView) rootView.findViewById(R.id.img_v);
            this.content = (TextView) rootView.findViewById(R.id.content);
        }

    }


}
