package com.kira.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kira on 2018/1/3/003.
 */

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private Unbinder mRootUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView==null)
        {
            int layoutId = getLayoutId();
            View rootView = inflater.inflate(layoutId, container, false);
            initWidget(rootView);
            rootView=mRootView;
        }else
        {
            if (mRootView.getParent()!=null)
            {
                ((ViewGroup)mRootView.getParent()).removeView(mRootView);
            }
        }
        return mRootView;
    }

    /**
     * 初始化控件
     * @param rootView
     */
    protected  void initWidget(View rootView){
        mRootUnBinder = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当View创建完后初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化布局
     * @return 返回布局参数
     */
    protected abstract int getLayoutId();

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPress()
    {
        return false;
    }
    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {

    }
}
