package com.kira.common.widget.recyler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kira.common.R;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kira on 2018/1/7/007.
 */

public abstract class RecylerAdapter<Data> extends RecyclerView.Adapter<RecylerAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener,IAdapterCallBack<Data> {
    private final List<Data> mList;
    private IAdapterListener<Data> mListener;

    public RecylerAdapter()
    {
        this(null);
    }
    public  RecylerAdapter(List<Data> list)
    {
        this(list,null);
    }
    public RecylerAdapter(List<Data> list,IAdapterListener<Data> listener)
    {
        this.mList=list;
        this.mListener=listener;
    }
    /**
     * 创建一个ViewHolder
     * @param parent
     * @param viewType 约定定为XML布局的ID
     * @return
     */

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        //让子类去复写创建ViewHolder
        ViewHolder<Data> holder=onCreateViewHolder(rootView,viewType);
        //将当前的View与ViewHolder进行绑定起来,设置View的Tag为ViewHolder,进行双向绑定
        rootView.setTag(R.id.tag_recyler_holder,holder);
        //设置事件点击
        rootView.setOnClickListener(this);
        rootView.setOnLongClickListener(this);
        //进行界面注解的绑定
        holder.mUnbinder = ButterKnife.bind(holder, rootView);
        //绑定CallBack
        holder.mCallBack=this;
        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     * @param view 根布局
     * @param viewType 布局类型,但现在约定为了XML的ID
     * @return 返回一个ViewHolder
     */
    protected abstract  ViewHolder<Data> onCreateViewHolder(View view,int viewType);

    /**
     * 与ViewHolder进行数据绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //得到需要绑定的数据
        Data data = mList.get(position);
        //触发Holder的绑定方法并与ViewHolder进行数据绑定
        holder.bind(data);
    }

    /**
     * 复写默认布局的返回
     * @param position 坐标
     * @return 类型,其实复写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position,mList.get(position));
    }

    /**
     * 得到布局的类型
     * @param position 坐标
     * @param data 当前的数据
     * @return XML文件的ID,用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position,Data data);

    @Override
    public int getItemCount() {
        if (mList==null || mList.size()==0)
        {
            return 0;
        }
        return mList.size();
    }

    /**
     * 插入一条数据并通知
     * @param data Data
     */
    public void add(Data data)
    {
        mList.add(data);
        notifyItemInserted(mList.size()-1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mList.size();
            Collections.addAll(mList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mList.size();
            mList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合,其中包括了清空
     * @param dataList
     */
    public void replace(Collection<Data> dataList)
    {
        mList.clear();
        if (dataList ==null || dataList.size()==0)
        {
            return;
        }
        mList.addAll(dataList);
        notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        ViewHolder<Data> viewHolder = (ViewHolder<Data>) view.getTag(R.id.tag_recyler_holder);
        if (mListener!=null)
        {
            int position = viewHolder.getAdapterPosition();
            mListener.onItemClick(viewHolder,mList.get(position));
        }

    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder<Data> viewHolder = (ViewHolder<Data>) view.getTag(R.id.tag_recyler_holder);
        if (mListener!=null)
        {
            int position = viewHolder.getAdapterPosition();
            mListener.onItemLongClick(viewHolder,mList.get(position));
        }
        return false;
    }

    /**
     * 自定义的监听器
     * @param <Data>
     */
    public interface IAdapterListener<Data>
    {
        void onItemClick(RecylerAdapter.ViewHolder holder,Data data);
        void onItemLongClick(RecylerAdapter.ViewHolder holder,Data data);
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void setOnListener(IAdapterListener listener)
    {
        this.mListener=listener;
    }

    /**
     * ViewHolder应该与界面的数据绑定在一起,所以要加一个泛型Data
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder mUnbinder;
        //将外界传进来的Data,保存在Holder里面.
        private Data mData;
        private IAdapterCallBack<Data> mCallBack;
        public ViewHolder(View itemView) {
            super(itemView);
        }
        void bind(Data data)
        {
            this.mData=data;
            onBind(data);
        }

        /**
         * 具体绑定内容交给子类来实现,Holder自己对自己的Data进行更新操作
         * @param data
         */

        protected abstract void onBind(Data data);
        public void updataData(Data data)
        {
            if (mCallBack!=null)
            {
                mCallBack.update(data,this);
            }
        }
    }
}
