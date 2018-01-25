package com.kira.wetalker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.kira.common.app.BaseActivity;
import com.kira.common.widget.view.PortraitView;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.appbar)
    View mAppBar;
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.im_search)
    ImageView mSearch;
    @BindView(R.id.btn_action)
    FloatActionButton mActionButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }
    @OnClick(R.id.im_search)
    public void onSearchMenuClick()
    {

    }
    @OnClick(R.id.btn_action)
    public void onActionButtonClick()
    {

    }
}
