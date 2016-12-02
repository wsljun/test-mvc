/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huixueyun.tifenwang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huixueyun.tifenwang.R;
import com.huixueyun.tifenwang.api.ApiResponse;
import com.huixueyun.tifenwang.core.ActionCallbackListener;
import com.huixueyun.tifenwang.model.model.LoginInfo;

/**
 * 登录
 *
 * @version 1.0
 * @date 16/3/2
 */
public class LoginActivity extends DBaseActivity {
    //输入账号框
    private EditText phoneEdit;
    //输入密码框
    private EditText passwordEdit;
    //登陆按钮
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerCloseListener(this);

        initViews();
    }

    // 初始化View
    private void initViews() {
        phoneEdit = (EditText) findViewById(R.id.edit_phone);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
    }

    // 准备登录
    public void toLogin(View view) {
        loginBtn.setEnabled(false);
        String loginName = phoneEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        accountAction.login(loginName, password, new LoginCallBackListener());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        //TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterCloseListener(this);
    }

    //登陆接口的回调
    class LoginCallBackListener implements ActionCallbackListener<ApiResponse<LoginInfo>> {

        @Override
        public void onSuccess(ApiResponse<LoginInfo> data) {
            startActivityAndFinish(MainActivity.class);
        }

        @Override
        public void onFailure(String errorEvent, String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            loginBtn.setEnabled(true);
        }
    }
}
