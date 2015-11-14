package com.smapley.powerwork.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smapley.powerwork.R;

/**
 * Created by smapley on 15/10/24.
 */
@ContentView(R.layout.activity_setting)
public class Setting extends BaseActivity {

    @ViewInject(R.id.tit_iv_left)
    private ImageView tit_iv_left;
    @ViewInject(R.id.tit_tv_center)
    private TextView tit_tv_center;
    @ViewInject(R.id.tit_iv_right)
    private ImageView tit_iv_right;

    @ViewInject(R.id.set_tv_account)
    private TextView set_tv_account;
    @ViewInject(R.id.set_tv_info)
    private TextView set_tv_info;
    @ViewInject(R.id.set_tv_feedback)
    private TextView set_tv_feedback;
    @ViewInject(R.id.set_tv_aboutus)
    private TextView set_tv_aboutus;


    @Override
    protected void initParams() {
        tit_iv_right.setVisibility(View.INVISIBLE);
        tit_tv_center.setText(R.string.setting);
    }

    @OnClick({R.id.tit_iv_left, R.id.set_tv_account, R.id.set_tv_info, R.id.set_tv_feedback, R.id.set_tv_aboutus})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tit_iv_left:
                finish();
                break;
            case R.id.set_tv_account:
                startActivity(new Intent(Setting.this, Account.class));
                break;
            case R.id.set_tv_info:
                startActivity(new Intent(Setting.this, Notification.class));
                break;
            case R.id.set_tv_feedback:
                startActivity(new Intent(Setting.this, Feedback.class));
                break;
            case R.id.set_tv_aboutus:
                startActivity(new Intent(Setting.this, AboutUs.class));
                break;
        }
    }
}
