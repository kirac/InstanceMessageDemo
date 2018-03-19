package com.kira.wetalker.frags.main;


import android.support.v4.app.Fragment;

import com.kira.common.app.BaseFragment;
import com.kira.common.widget.view.GalleyView;
import com.kira.wetalker.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends BaseFragment {
    @BindView(R.id.galleryview)
    GalleyView mGalleyView;
    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData() {
        super.initData();
        mGalleyView.setUp(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedChange(int count) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_active;
    }

}
