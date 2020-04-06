package com.aliyun.iot.ilop.demo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.iot.aep.oa.page.OAMobileCountrySelectorActivity;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.aliyun.iot.demo.R;
import com.aliyun.iot.ilop.demo.page.debug.Env;
import com.aliyun.iot.ilop.demo.page.login3rd.OALoginActivity;
import com.facebook.FacebookSdk;

import static com.aliyun.iot.aep.sdk.IoTSmart.REGION_ALL;

public class SDKInitHelper {
    private static final String TAG = "SDKInitHelper";

    public static void init(AApplication app) {
        if (app == null) {
            return;
        }
        preInit(app);
        onInit(app);
        //onInitDefault(app);
        postInit(app);
    }

    /**
     * 初始化之前准备工作
     * @param app
     */
    private static void preInit(AApplication app) {
        //要在OA初始化前调用
        ConfigManager.getInstance().setGoogleClientId("29196877");

        String appId = "29196877";
        FacebookSdk.setApplicationId(appId);
        ConfigManager.getInstance().setFacebookId(appId);
    }

    /**
     * 默认初始化
     * @param app
     */
    private static void onInitDefault(AApplication app) {
        GlobalConfig.getInstance().setApiEnv(GlobalConfig.API_ENV_PRE);
        GlobalConfig.getInstance().setBoneEnv(GlobalConfig.BONE_ENV_TEST);

        IoTSmart.init(app);
    }
    /**
     * 带参数初始化
     * @param app
     */
    private static void onInit(AApplication app) {
        // 默认的初始化参数
        IoTSmart.InitConfig initConfig = new IoTSmart.InitConfig()
                // REGION_ALL: 支持连接中国大陆和海外多个接入点，REGION_CHINA_ONLY:直连中国大陆接入点，只在中国大陆出货选这个
                .setRegionType(REGION_ALL)
                // 对应控制台上的测试版（PRODUCT_ENV_DEV）和正式版（PRODUCT_ENV_PROD）(默认)
                .setProductEnv(IoTSmart.PRODUCT_ENV_DEV)
                // 是否打开日志
                .setDebug(true);

        // 定制三方通道离线推送，目前支持华为、小米和FCM
        IoTSmart.PushConfig pushConfig = new IoTSmart.PushConfig();
        pushConfig.fcmApplicationId = "fcmid"; // 替换为从FCM平台申请的id
        pushConfig.fcmSendId = "fcmsendid"; // 替换为从FCM平台申请的sendid
        pushConfig.xiaomiAppId = "XiaoMiAppId"; // 替换为从小米平台申请的AppID
        pushConfig.xiaomiAppkey = "XiaoMiAppKey"; // 替换为从小米平台申请的AppKey
        // 华为、vivo推送通道需要在AndroidManifest.xml里面添加
        initConfig.setPushConfig(pushConfig);


        GlobalConfig.getInstance().setApiEnv(GlobalConfig.API_ENV_ONLINE);
        GlobalConfig.getInstance().setBoneEnv(GlobalConfig.BONE_ENV_TEST);

        ALog.d(TAG, "initConfig1:" + JSON.toJSONString(initConfig));
        // 切换国内，国外环境，测试版、正式版环境后的初始化参数
        Env env = Env.getInstance();
        ALog.d(TAG, "env:" + env.toString());
        if (env.isSwitched()) {
            if (!TextUtils.isEmpty(env.getApiEnv())) {
                GlobalConfig.getInstance().setApiEnv(env.getApiEnv());
            }
            if (!TextUtils.isEmpty(env.getBoneEnv())) {
                GlobalConfig.getInstance().setBoneEnv(env.getBoneEnv());
            }
            if (!TextUtils.isEmpty(env.getProductEnv())) {
                initConfig.setProductEnv(env.getProductEnv());
            }

            ALog.d(TAG, "initConfig2:" + JSON.toJSONString(initConfig));
        }
        // 初始化
        IoTSmart.init(app, initConfig);

        new AlcsCoAP().setLogLevel(ALog.LEVEL_ERROR);

    }

    /**
     * 初始化后的定制参数
     *
     * @param app application
     */
    private static void postInit(@SuppressWarnings("unused") AApplication app) {

        OALoginAdapter adapter = (OALoginAdapter) LoginBusiness.getLoginAdapter();
        if (adapter != null) {
            adapter.setDefaultLoginClass(OALoginActivity.class);
        }
        OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.AccountPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;

        OpenAccountUIConfigs.ChangeMobileFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.ChangeMobileFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;

        OpenAccountUIConfigs.MobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;

        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;

        OpenAccountUIConfigs.OneStepMobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
    }
}
