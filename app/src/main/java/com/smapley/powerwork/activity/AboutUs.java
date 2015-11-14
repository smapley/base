package com.smapley.powerwork.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smapley.powerwork.R;

/**
 * Created by smapley on 15/10/24.
 */
@ContentView(R.layout.activity_aboutus)
public class AboutUs extends BaseActivity {

    @ViewInject(R.id.tit_iv_left)
    private ImageView tit_iv_left;
    @ViewInject(R.id.tit_tv_center)
    private TextView tit_tv_center;
    @ViewInject(R.id.tit_iv_right)
    private ImageView tit_iv_right;

    @ViewInject(R.id.abo_bt_good)
    private Button abo_bt_good;


    @Override
    protected void initParams() {

        tit_iv_right.setVisibility(View.INVISIBLE);
        tit_tv_center.setText(R.string.aboutus);

    }

    @OnClick({R.id.abo_bt_good, R.id.tit_iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tit_iv_left:
                finish();
                break;
            case R.id.abo_bt_good:
                showToast("好评~");
                break;
        }
    }


}
