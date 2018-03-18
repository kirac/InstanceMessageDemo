package com.kira.common.widget.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kira.common.R;
import com.kira.common.widget.recyler.RecylerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kira on 2018/3/18/018.
 */

public class GalleyView extends RecyclerView {
    private static final int LOADER_ID=0x0100;
    private static final int MAX_IMAGE_COUNT = 3;
    private static final long MIN_IMAGE_FILE_LENGTH = 10 * 1024;
    private Adapter mAdapter;
    private LoaderCallBack mLoaderCallBack=new LoaderCallBack();
    private List<Image> mSelectedImages=new LinkedList<>();
    private SelectedChangeListener mSelectedChangeListener;
    public GalleyView(Context context) {
        super(context);
        init();
    }



    public GalleyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(),4));
        setAdapter(mAdapter);
        //设置点击事件
        mAdapter.setOnListener(new RecylerAdapter.IAdapterListener<Image>() {
            @Override
            public void onItemClick(RecylerAdapter.ViewHolder holder, Image image) {
                //Cell点击操作,如果点击是允许的,那么更新对应Cell的状态
                //然后更新界面,同理:如果说不能允许点击(已经达到最大的选中数量)那么就不刷新界面
                if (onItemSelectClick(image))
                {
                    holder.updataData(image);
                }
            }

            @Override
            public void onItemLongClick(RecylerAdapter.ViewHolder holder, Image image) {

            }
        });
    }

    /**
     * 初始化方法
     * @param loaderManager Loader管理器
     * @param listener CELL状态改变监听者
     * @return 任何一个LOADER_ID,可用于销毁Loader
     */
    public int setUp(LoaderManager loaderManager,SelectedChangeListener listener)
    {
        this.mSelectedChangeListener=listener;
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallBack);
        return LOADER_ID;
    }

    /**
     * 得到选中的图片的全部地址
     * @return 返回一个数组
     */
    public String[] getSelectedPath()
    {
        String[] paths=new String[mSelectedImages.size()];
        int index=0;
        for(Image image:mSelectedImages)
        {
            paths[index++] = image.path;
        }
        return paths;
    }
    public void clear()
    {
        for (Image selectedImage : mSelectedImages) {
            //将状态全部重置
            selectedImage.isSelect=false;
        }
        mSelectedImages.clear();
        //通知更新
        mAdapter.notifyDataSetChanged();
    }
    /**
     * 用于实际的数据加载的Loader Callback
     */
    private class LoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor>
    {
        private final String[] IMAGE_PROJECTION=new String[]{
                MediaStore.Images.Media._ID, //ID
                MediaStore.Images.Media.DATA, //图片路径
                MediaStore.Images.Media.DATE_ADDED //图片的创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            //创建一个Loader
            //如果是我们的ID,则可以进行初始化
            if (i==LOADER_ID)
            {
                return new CursorLoader(getContext()
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        ,IMAGE_PROJECTION
                        ,null
                        ,null
                        ,IMAGE_PROJECTION[2]+" DESC"); //倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            //当Loader加载完成时
            List<Image> images = new ArrayList<>();
            //判断是否有数据
            if (cursor!=null)
            {
                int count = cursor.getCount();
                if (count>0)
                {
                    //移动游标到开始
                     cursor.moveToFirst();
                     //得到对应的列的Index坐标
                    int indexId = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        //循环读取,直到没有下一条数据
                        int id = cursor.getInt(indexId);
                        String path = cursor.getString(indexPath);
                        long dateTime = cursor.getLong(indexDate);
                        File file = new File(path);
                        if (!file.exists() || file.length()<MIN_IMAGE_FILE_LENGTH)
                        {
                            //如果没有图片,或者图片大小太小,则跳过
                            continue;
                        }

                        //添加一条新的数据
                        Image image = new Image();
                        image.id=id;
                        image.path=path;
                        image.date=dateTime;
                        mSelectedImages.add(image);
                    }while (cursor.moveToNext());

                }
            }
            updateSource(mSelectedImages);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //当Loader销毁或者重置了
        }
    }

    /**
     * 通知Adapter数据更改的方法
     * @param selectedImages 新的数据
     */
    private void updateSource(List<Image> selectedImages) {
        mAdapter.replace(selectedImages);
    }

    /**
     * Cell点击的具体逻辑
     * @param image
     * @return True代表我进行了数据更改,你需要刷新;反之不刷新
     */
    private boolean  onItemSelectClick(Image image)
    {
        //是否需要刷新
        boolean isRefresh;
        if (mSelectedImages.contains(image))
        {
            //如果之前存在,那么现在移除
            mSelectedImages.remove(image);
            image.isSelect= false;
            //状态已经改变则需要刷新
            isRefresh=true;
        }else{
            if (mSelectedImages.size()>=MAX_IMAGE_COUNT)
            {
                isRefresh=false;
            }else{
                mSelectedImages.add(image);
                image.isSelect=true;
                isRefresh=true;
            }
        }
        if (isRefresh)
        {
            //如果数据有更改,,则需要通知外面的监听者:数据选中改变了
            notifySelectChanged();
        }
         return true;
    }

    /**
     * 通知状态改变
     */
    private void notifySelectChanged() {
        //得到监听者,并判断是否有监听者,然后进行回调数量变化
        if (mSelectedChangeListener!=null)
        {
            mSelectedChangeListener.onSelectedChange(mSelectedImages.size());
        }
    }

    /**
     * 改变状态监听者
     */
    public interface SelectedChangeListener
    {
        void onSelectedChange(int count);
    }

    /**
     * 内部的数据结构
     */
    private static class  Image{
        int id; //数据ID
        String path; //图片的路径
        long date; //图片的创建日期
        boolean isSelect //图片是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }
    private static class Adapter extends RecylerAdapter<Image>
    {


        @Override
        protected ViewHolder<Image> onCreateViewHolder(View view, int viewType) {
            return new GalleyView.ViewHolder(view);
        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }
    }
    private  class ViewHolder extends RecylerAdapter.ViewHolder<Image>
    {

        private  ImageView mPic;
        private  View mShade;
        private  CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) findViewById(R.id.im_image);
            mShade = findViewById(R.id.view_shade);
            mSelected = (CheckBox) findViewById(R.id.cb_select);

        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path) //加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //因为加载的不是网络图片,所以不使用缓存,直接从原图加载
                    .centerCrop() //居中剪切
                    .placeholder(R.color.green_200) //默认图片
                    .into(mPic);
            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);
        }
    }
}
