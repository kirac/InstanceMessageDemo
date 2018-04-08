package com.kira.wetalker;

import com.kira.common.app.BaseActivity;
import com.kira.wetalker.frags.assist.PermissionsFragment;


public class LaunchActivity extends BaseActivity {

    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }
}
