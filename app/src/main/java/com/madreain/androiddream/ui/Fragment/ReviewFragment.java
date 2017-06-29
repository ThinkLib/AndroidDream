package com.madreain.androiddream.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madreain.androiddream.R;
import com.madreain.androiddream.core.Manager.TypeManager;
import com.madreain.androiddream.core.Manager.callback.MBCallback;
import com.madreain.androiddream.core.Model.MType;
import com.madreain.androiddream.library.kprogresshud.KProgressHUD;
import com.madreain.androiddream.ui.Activity.AboutActivity;
import com.madreain.androiddream.ui.Activity.SecondTypeActivity;
import com.madreain.androiddream.ui.Activity.ShareKnowledgeActivity;
import com.madreain.androiddream.ui.Activity.TypeDetailsActivity;
import com.madreain.androiddream.utils.NetworkUtils;
import com.madreain.androiddream.views.CircleMenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 复习界面
 *
 * @author madreain
 * @time 2017/4/5
 */

public class ReviewFragment extends Fragment {
    private TextView txt_title;
    private ImageView img_return;
    private ImageView img_add;
    private View rootview = null;
    private Context mContext;
    List<MType> mTypeList;
    private CircleMenuLayout circleMenuLayout;
    private ImageView img_center;

    KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_review, null);
        mContext = getContext();
        initView(rootview);
        //判断网络如果有网络选择网络刷新
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            refreshData();
        }else {
            setLocalData();
        }
//        refreshData();
//        setMenuData();
        initListener();
        return rootview;
    }

    private void setLocalData() {
        mTypeList = TypeManager.getInstance().getMType();
        if(mTypeList==null||mTypeList.size()==0){
            return;
        }
        String[] mItemTexts = new String[mTypeList.size()];
        for (int i = 0; i < mTypeList.size(); i++) {
            mItemTexts[i] = mTypeList.get(i).getTitle();
        }

        int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
                R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
                R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
                R.drawable.home_mbank_6_normal, R.drawable.home_mbank_1_normal};
        circleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
    }

    private void refreshData() {
        kProgressHUD = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();

        TypeManager.getInstance().refreshMType(new MBCallback.MBValueCallBack<List<MType>>() {
            @Override
            public void onSuccess(List<MType> result) {
                mTypeList = result;
                String[] mItemTexts = new String[mTypeList.size()];
                for (int i = 0; i < mTypeList.size(); i++) {
                    mItemTexts[i] = mTypeList.get(i).getTitle();
                }

                int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
                        R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
                        R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
                        R.drawable.home_mbank_6_normal, R.drawable.home_mbank_1_normal};
                circleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
                kProgressHUD.dismiss();
            }

            @Override
            public void onError(String code) {
                kProgressHUD.dismiss();
            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void initView(View rootview) {
        mTypeList = new ArrayList<>();
        txt_title = (TextView) rootview.findViewById(R.id.txt_title);
        img_return = (ImageView) rootview.findViewById(R.id.img_return);
        img_add = (ImageView) rootview.findViewById(R.id.img_add);
        //设置actionbar
        txt_title.setText("复习");
        img_return.setVisibility(View.GONE);
        circleMenuLayout = (CircleMenuLayout) rootview.findViewById(R.id.circleMenuLayout);
        img_center = (ImageView) rootview.findViewById(R.id.img_center);
    }

    private void setMenuData() {
        //显示存在着一个bug 当显示6个时 第一个会和第六个重叠在一起显示
        mTypeList = TypeManager.getInstance().getDefaultMType();
        String[] mItemTexts = new String[mTypeList.size()];
        for (int i = 0; i < mTypeList.size(); i++) {
            mItemTexts[i] = mTypeList.get(i).getTitle();
        }

        int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
                R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
                R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
                R.drawable.home_mbank_6_normal};
        circleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

    }

    private void initListener() {
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                if (pos == 6) {
                    pos = 0;
                }
                MType mType = mTypeList.get(pos);
                Intent intent;
                if (mType != null && mType.getTitle() != null && (mType.getTitle().equals("开源项目") || mType.getTitle().equals("面试"))) {
                    intent = new Intent(mContext, TypeDetailsActivity.class);
                } else {
                    intent = new Intent(mContext, SecondTypeActivity.class);
                }
                intent.putExtra("mType", mType);
//                intent.putExtra("mid", mType.getMid());
                startActivity(intent);

            }

            @Override
            public void itemCenterClick(View view) {

            }
        });
        //点击icon 进入关于界面
        img_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
        // 增加一个提交推荐内容给掘梦
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ShareKnowledgeActivity.class));
            }
        });
    }

    //刷新的方法
    public void refreshFragmentData() {

    }

}
