package com.smapley.powerwork.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smapley.powerwork.utils.MyData;
import com.smapley.powerwork.R;

/**
 * Created by smapley on 15/10/23.
 */
@ContentView(R.layout.activity_feedback)
public class Feedback extends BaseActivity {

    @ViewInject(R.id.tit_iv_left)
    private ImageView tit_iv_left;
    @ViewInject(R.id.tit_tv_center)
    private TextView tit_tv_center;
    @ViewInject(R.id.tit_iv_right)
    private ImageView tit_iv_right;

    @ViewInject(R.id.fee_iv_pic)
    private ImageView fee_iv_pic;
    @ViewInject(R.id.fee_tv_name)
    private TextView fee_tv_name;
    @ViewInject(R.id.fee_et_content)
    private EditText fee_et_content;
    @ViewInject(R.id.fee_bt_submit)
    private Button fee_bt_submit;


    @Override
    protected void initParams() {
        tit_iv_right.setVisibility(View.INVISIBLE);
        tit_tv_center.setText(R.string.feedback);
    }

    @OnClick({R.id.tit_iv_left, R.id.fee_bt_submit})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tit_iv_left:
                finish();
                break;
            case R.id.fee_bt_submit:
                submit();
                break;
        }
    }

    private void submit() {
        String details = fee_et_content.getText().toString();
        if (!details.equals("")) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("username", "smapley");
            params.addBodyParameter("password", "smapley");
            httpUtils.send(HttpRequest.HttpMethod.POST, MyData.URL_LOGIN, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    LogUtils.i(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    LogUtils.i(msg);

                }
            });
        } else {
            showToast(R.string.fee_content_null);
        }
    }
}
