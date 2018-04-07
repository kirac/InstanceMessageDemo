package com.kira.wetalker.frags.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.kira.common.app.BaseFragment;
import com.kira.common.app.MyApplication;
import com.kira.common.widget.view.PortraitView;
import com.kira.wetalker.R;
import com.kira.wetalker.frags.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 * Created by kira on 2018/4/6/006.
 */

public class UpdateInfoFragment extends BaseFragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update_info;
    }
    @OnClick(R.id.im_portrait)
    public void onPortraitView()
    {
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式JPEG
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        //得到头像的缓存地址
                        File portraitTmpFile = MyApplication.getPortraitTmpFile();
                        //发起剪切
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(portraitTmpFile))
                                .withAspectRatio(1,1) //1比1比例
                                .withMaxResultSize(520,520) //返回最大的尺寸
                                .withOptions(options) //相关参数
                                .start(getActivity());

                    }
                }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从Activity中传递过来的回调.然后取出其中的值进行图片回调
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //通过Ucrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri!=null)
            {
                loadProtrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri到对应的头像中
     * @param resultUri
     */
    private void loadProtrait(Uri resultUri) {
        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortraitView);
    }
}
