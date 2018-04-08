package com.kira.wetalker.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kira.common.tools.UiTool;
import com.kira.common.widget.view.GalleyView;
import com.kira.wetalker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleyView.SelectedChangeListener{

    private GalleyView mGalleyView;
    private OnSelectedListener mListener;
    public GalleryFragment() {
        // Required empty public constructor

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new TransStatusBottomSheetDialog(getContext());
    }

    /**
     * 设置事件监听并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener)
    {
        this.mListener=listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取定义好的GalleryView
        View root= inflater.inflate(R.layout.fragment_gallery, container, false);
        mGalleyView= root.findViewById(R.id.fragment_galleryview);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleyView.setUp(getLoaderManager(),this);
    }

    @Override
    public void onSelectedChange(int count) {
        //如果选中一张图片
        if (count>0)
        {
            //隐藏自己
            dismiss();
            if (mListener!=null)
            {
                String[] selectedPath = mGalleyView.getSelectedPath();
                mListener.onSelectedImage(selectedPath[0]);
                //取消和调用者之间的引用,加快内存回收
                mListener=null;
            }
        }
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener
    {
        void onSelectedImage(String path);
    }
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog
    {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if (window==null)
            {
                return;
            }
            //得到屏幕的高度
            int heightPixels = UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());
            int dialogHeight = heightPixels - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,dialogHeight<=0?ViewGroup.LayoutParams.MATCH_PARENT:dialogHeight);

        }
    }
}
