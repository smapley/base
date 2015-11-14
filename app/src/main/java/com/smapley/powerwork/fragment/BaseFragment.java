package com.smapley.powerwork.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import java.net.ProtocolException;

/**
 * Created by smapley on 15/10/22.
 */
public abstract class BaseFragment extends Fragment{

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(getLayoutId(),container,false);
        ViewUtils.inject(this, view);
        initParams();
        return view;
    }

    /**
     * 初始化布局
     */
    protected abstract int getLayoutId();

    /**
     * 设置参数
     */
    protected abstract void initParams();
}
