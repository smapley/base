package com.smapley.powerwork.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.smapley.powerwork.R;

import java.net.URI;
import java.util.List;

import cn.pedant.SweetAlert.OptAnimationLoader;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by smapley on 15/10/24.
 */
@ContentView(R.layout.activity_account)
public class Account extends BaseActivity {

    @ViewInject(R.id.tit_iv_left)
    private ImageView tit_iv_left;
    @ViewInject(R.id.tit_tv_center)
    private TextView tit_tv_center;
    @ViewInject(R.id.tit_iv_right)
    private ImageView tit_iv_right;


    @ViewInject(R.id.acc_iv_pic)
    private ImageView acc_iv_pic;
    @ViewInject(R.id.acc_iv_changepic)
    private ImageView acc_iv_changepic;
    @ViewInject(R.id.acc_et_name)
    private EditText acc_et_name;
    @ViewInject(R.id.acc_et_phone)
    private EditText acc_et_phone;
    @ViewInject(R.id.acc_et_birthday)
    private EditText acc_et_birthday;

    @Override
    protected void initParams() {
        tit_iv_right.setVisibility(View.INVISIBLE);
        tit_tv_center.setText(R.string.setting);
        acc_et_name.setText(sp_user.getString("name", ""));
        acc_et_phone.setText(sp_user.getString("phone", ""));
        acc_et_birthday.setText(sp_user.getString("birthday",""));
    }


    @OnClick({R.id.tit_iv_left, R.id.acc_iv_changepic})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tit_iv_left:
                finish();
                break;
            case R.id.acc_iv_changepic:

                new SweetAlertDialog(Account.this)
                        .setTitleText(getString(R.string.acc_dialog_title))
                        .setConfirmText(getString(R.string.acc_dialog_ok))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                selectPic();
                                sweetAlertDialog.dismiss();
                            }
                        }).showCancelButton(true).show();


                break;
        }
    }

    /**
     * 从相册选择头像
     */
    private void selectPic(){
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        boolean showCamera = true;
        int maxNum = 1;
        Intent intent = new Intent(Account.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
//                if (mSelectPath != null && mSelectPath.size() > 0) {
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
//                }
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 0) {
                List<String> resultList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Uri uri= Uri.parse(resultList.get(0));
                acc_iv_pic.setImageURI(uri);
            }
        } catch (Exception e) {

        }
    }

    private void saveData(){
        SharedPreferences.Editor editor=sp_user.edit();
        editor.putString("name",acc_et_name.getText().toString());
        editor.putString("phone",acc_et_phone.getText().toString());
        editor.putString("birthday",acc_et_birthday.getText().toString());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }
}
