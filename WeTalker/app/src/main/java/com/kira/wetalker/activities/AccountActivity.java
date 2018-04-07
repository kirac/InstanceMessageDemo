package com.kira.wetalker.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kira.common.app.BaseActivity;
import com.kira.wetalker.R;
import com.kira.wetalker.frags.account.UpdateInfoFragment;

public class AccountActivity extends BaseActivity {
    private Fragment mCurFragment;

    /**
     * 账户Activity显示的入口
     * @param context
     */
    public static void show(Context context)
    {
        context.startActivity(new Intent(context,AccountActivity.class));
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.account_container,mCurFragment)
                .commit();
    }
    //Activity中收到剪切图片成功的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       mCurFragment.onActivityResult(requestCode,resultCode,data);
    }

}
