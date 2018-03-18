package com.kira.wetalker.helper;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * Created by kira on 2018/2/27/027.
 * 解决Fragment的调度与重用问题,达到最优的Fragment切换
 */

public class NavHelper<T> {

    //用于初始化的参数
    /**
     * 上下文
     */
    private final Context mContext;
    /**
     * 容器ID
     */
    private final int mContainerId;
    /**
     * Fragment管理器
     */
    private final FragmentManager mFragmentManager;
    private OnTabChangeListener mOnTabChangeListener;
    private SparseArray<Tab<T>> mTabSparseArray=new SparseArray<>();
    //当前的一个选中Tab
    private Tab<T> mCurrentTab;
    public NavHelper(Context context, int containerId,
                     FragmentManager fragmentManager,OnTabChangeListener listener) {
        mContext = context;
        mContainerId = containerId;
        mFragmentManager = fragmentManager;
        mOnTabChangeListener=listener;

    }

    public boolean performClickMenu(int menuId)
    {
        //集合寻找点击的菜单对应的Tab
        //如果有则处理
        Tab<T> tab = mTabSparseArray.get(menuId);
        if (tab!=null)
        {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的Tab选择操作
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab=null;
        if (mCurrentTab!=null)
        {
            oldTab=mCurrentTab;
            if (oldTab==tab)
            {
                //如果说当前的tab就是点击的Tab,那么就不做处理
                notifyTabReselect(tab);
                return;
            }
        }
        //赋值并调用切换方法
        mCurrentTab=tab;
        doTabChanged(mCurrentTab,oldTab);
    }

    private void doTabChanged(Tab<T> currentTab, Tab<T> oldTab) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (oldTab!=null)
        {
            if (oldTab.fragment!=null)
            {
                //从界面移除,但是还是在Fragment的缓存空间中
                fragmentTransaction.detach(oldTab.fragment);

            }
        }
        if (currentTab!=null)
        {
            if (currentTab.fragment==null)
            {
                //首次新建
                Fragment fragment = Fragment.instantiate(mContext, currentTab.clx.getName(), null);
                //缓存起来
                currentTab.fragment=fragment;
                //提交到FragmentManager
                fragmentTransaction.add(mContainerId,fragment,currentTab.clx.getName());

            }else{
                //从FragmentManager的缓存空间中重新加载到界面中
                fragmentTransaction.attach(currentTab.fragment);
            }

        }
        //提交事务
        fragmentTransaction.commit();
        //通知回调
        notifyTabSelect(currentTab,oldTab);
    }

    private void notifyTabSelect(Tab<T> currentTab, Tab<T> oldTab) {
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChange(currentTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab) {
        //二次点击Tab所做的操作,可以是刷新界面
    }

    /**
     * 添加Tab
     * @param menuId   Tab对应的菜单Id
     * @param tab  Tab
     * @return 返回自己用于链式编程
     */
    public NavHelper<T> add(int menuId,Tab<T> tab)
    {
        mTabSparseArray.put(menuId,tab);
        return this;
    }
    /**
     * 获取当前显示的Tab
     * @return 当前的Tab
     */
    public Tab<T> getCurrentTab()
    {
        return mCurrentTab;
    }
    /**
     * 我们的所有的Tab基础属性
     *
     * @param <T> 范型的额外参数
     */
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        // Fragment对应的Class信息
        public Class<?> clx;
        // 额外的字段，用户自己设定需要使用
        public T extra;

        // 内部缓存的对应的Fragment，
        // Package权限，外部无法使用
        Fragment fragment;
    }
    /**
     * 定义事件处理完成后的回调
     */
    public interface OnTabChangeListener<T>
    {
       void onTabChange(Tab<T> newTab,Tab<T> oldTab);
    }

}

