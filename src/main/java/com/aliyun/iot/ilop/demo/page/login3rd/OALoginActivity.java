package com.aliyun.iot.ilop.demo.page.login3rd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.sdk.android.oauth.OauthPlateform;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.aliyun.iot.aep.oa.OALanguageUtils;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.demo.R;
import com.aliyun.iot.ilop.demo.dialog.RegisterSelectorDialogFragment;
import com.aliyun.iot.ilop.demo.dialog.ResetSelectorDialogFragment;
import com.aliyun.iot.ilop.demo.page.debug.EnvironmentActivity;
import com.aliyun.iot.ilop.demo.page.ilopmain.MainActivity;
import com.aliyun.iot.link.ui.component.LinkToast;


public class OALoginActivity extends LoginActivity implements View.OnClickListener, AuthCodeFragment.Listener {

    private RegisterSelectorDialogFragment registerSelectorDialogFragment;
    private ResetSelectorDialogFragment resetSelectorDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //显示登录页和手机忘记密码页的选择国家区号
        OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.supportForeignMobileNumbers = true;
        super.onCreate(savedInstanceState);
        TRANSPARENT();
//        mToolBar.setLogo(R.drawable.back_arrow);
        findViewById(R.id.btn_gmail).setOnClickListener(this);
//        findViewById(R.id.btn_facebook).setOnClickListener(this);


        registerSelectorDialogFragment = new RegisterSelectorDialogFragment();
        registerSelectorDialogFragment.setOnClickListener(registerListenr);

        resetSelectorDialogFragment = new ResetSelectorDialogFragment();
        resetSelectorDialogFragment.setOnClickListener(resetListenr);


        this.resetPasswordTV = this.findViewById(ResourceUtils.getRId(this, "reset_password"));
        if (this.resetPasswordTV != null) {
            this.resetPasswordTV.setOnClickListener(v -> resetSelectorDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), ""));
        }
        this.registerTV = this.findViewById(ResourceUtils.getRId(this, "register"));
        if (this.registerTV != null) {
            this.registerTV.setOnClickListener(v -> registerSelectorDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), ""));
        }

        findViewById(R.id.switch_env).setOnClickListener(view -> EnvironmentActivity.show(view.getContext()));

        findViewById(R.id.switch_env).setOnLongClickListener(view -> false);

        findViewById(R.id.authcode_login).setOnClickListener(view -> new AuthCodeFragment().show(getSupportFragmentManager(), "1576063472"));
    }


    @Override
    public void onClick(View v) {
        OauthService oauthService = OpenAccountSDK.getService(OauthService.class);
        int oaCode = OauthPlateform.GOOGLE;
        if (v.getId() == R.id.btn_gmail) {
            oaCode = OauthPlateform.GOOGLE;
            //            case R.id.btn_facebook:
//                oaCode = OauthPlateform.FACEBOOK;
//                break;
        }
        try {
            oauthService.oauth(this, oaCode, new LoginCallback() {
                @Override
                public void onSuccess(OpenAccountSession session) {
                    LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                    if (loginCallback != null) {
                        loginCallback.onSuccess(session);
                    }
                    OALoginActivity.this.finishWithoutCallback();
                }

                @Override
                public void onFailure(int code, String msg) {
                    Log.e(TAG, "onFailure: " + msg);
                    Toast.makeText(OALoginActivity.this, "oauth 失败 code = " + code + " message = " + msg, Toast.LENGTH_LONG).show();

                    LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                    if (loginCallback != null) {
                        loginCallback.onFailure(code, msg);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private View.OnClickListener registerListenr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_register_phone) {//手机注册
                OpenAccountUIService openAccountUIService = OpenAccountSDK.getService(OpenAccountUIService.class);
                openAccountUIService.showRegister(OALoginActivity.this, getRegisterLoginCallback());
                registerSelectorDialogFragment.dismissAllowingStateLoss();
            } else if (v.getId() == R.id.btn_register_email) {//邮箱注册
                OpenAccountUIService openAccountUIService = OpenAccountSDK.getService(OpenAccountUIService.class);
                openAccountUIService.showEmailRegister(OALoginActivity.this, getEmailRegisterCallback());
                registerSelectorDialogFragment.dismissAllowingStateLoss();
            }
        }
    };


    private View.OnClickListener resetListenr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_register_phone) {//手机找回
                forgetPhonePassword(v);
                resetSelectorDialogFragment.dismissAllowingStateLoss();
            } else if (v.getId() == R.id.btn_register_email) {//邮箱找回
                forgetMailPassword(v);
                resetSelectorDialogFragment.dismissAllowingStateLoss();
            }
        }
    };

    public void forgetPhonePassword(View view) {
        super.forgetPassword(view);
    }

    public void forgetMailPassword(View view) {
        OpenAccountUIService openAccountUIService = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
        openAccountUIService.showEmailResetPassword(this, this.getEmailResetPasswordCallback());
    }

    private EmailResetPasswordCallback getEmailResetPasswordCallback() {
        return new EmailResetPasswordCallback() {

            @Override
            public void onSuccess(OpenAccountSession session) {
                LoginCallback callback = getLoginCallback();
                if (callback != null) {
                    callback.onSuccess(session);

                }
                finishWithoutCallback();
            }

            @Override
            public void onFailure(int code, String message) {
                LoginCallback callback = getLoginCallback();
                if (callback != null) {
                    callback.onFailure(code, message);
                }
            }

            @Override
            public void onEmailSent(String email) {
                Toast.makeText(getApplicationContext(), email + " 已经发送了", Toast.LENGTH_LONG).show();
            }

        };
    }

    private EmailRegisterCallback getEmailRegisterCallback() {
        return new EmailRegisterCallback() {

            @Override
            public void onSuccess(OpenAccountSession session) {
                LoginCallback callback = getLoginCallback();
                if (callback != null) {
                    callback.onSuccess(session);
                }
                finishWithoutCallback();
            }

            @Override
            public void onFailure(int code, String message) {
                LoginCallback callback = getLoginCallback();
                if (callback != null) {
                    callback.onFailure(code, message);
                }
            }

            @Override
            public void onEmailSent(String email) {
                Toast.makeText(getApplicationContext(), email + " 已经发送了", Toast.LENGTH_LONG).show();
            }

        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OauthService service = OpenAccountSDK.getService(OauthService.class);
        if (service != null) {
            service.authorizeCallback(requestCode, resultCode, data);
        }
    }


    protected final void TRANSPARENT() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(OALanguageUtils.attachBaseContext(newBase));
    }

    @Override
    protected String getLayoutName() {
        return "ali_sdk_openaccount_login2";
    }

    @Override
    public void onAuthCodeClicked(int position) {
        try {
            String code = AuthCodeFragment.STATIC_AUTH_CODES[position];
            LoginBusiness.authCodeLogin(code, new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    MainActivity.start(OALoginActivity.this);
                    finish();
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onLoginFailed(int i, String s) {
                    ALog.d(TAG, "code: " + i + ", str: " + s);
                    LinkToast.makeText(OALoginActivity.this, s).show();
                }
            });
        } catch (IndexOutOfBoundsException ignored) {

        }
    }
}