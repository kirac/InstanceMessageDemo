package com.kira.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kira on 2018/1/3/003.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当界面未初始化之前调用的初始化窗口
        initWindow();
        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getLayoutId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    protected void initWindow() {

    }
    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 初始化布局
     * @return 返回布局参数
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected void initData()
    {

    }

    /**
     * 初始化控件
     */
    protected void initWidget()
    {
        ButterKnife.bind(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击导航返回时,finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments!=null && fragments.size()>0)
        {
            for (Fragment fragment : fragments) {
                //判断是否我们能够处理的Fragment的类型
                if (fragment instanceof BaseFragment)
                {
                    //判断是否拦截了返回按钮
                    if (((BaseFragment) fragment).onBackPress())
                    {
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
