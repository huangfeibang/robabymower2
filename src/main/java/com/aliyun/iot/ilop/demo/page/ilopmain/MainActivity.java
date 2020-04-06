package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.component.scan.ScanManager;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.demo.R;
import com.aliyun.iot.ilop.demo.page.device.bind.BindAndUseActivity;
import com.aliyun.iot.ilop.demo.page.device.scan.AddDeviceScanPlugin;
import com.aliyun.iot.ilop.startpage.list.main.countryselect.CountryListActivity;

import java.util.Arrays;

public class MainActivity extends FragmentActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private String TAG = "MainActivity";

    private Class[] fragmentClass = {HomeTabFragment.class, MyDeviceMainFragment.class, FeedbackFragment.class, MyAccountTabFragment.class};
    private String[] textViewArray = {"Home","Device", "Feedback", "User"};
    private Integer[] drawables = {R.drawable.tab_home_btn, R.drawable.tab_view_btn, R.drawable.tab_view_feedback, R.drawable.tab_my_btn};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(getApplicationContext())) {
//                //启动Activity让用户授权
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 100);
//            } else {
//                FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
//                if (helper != null) {
//                    helper.setNeedShowFloatWindowFlag(true);
//                }
//            }
//        } else {
//            FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
//            if (helper != null) {
//                helper.setNeedShowFloatWindowFlag(true);
//            }
//        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        MyFragmentTabLayout fragmentTabHost = findViewById(R.id.tab_layout);
        fragmentTabHost.init(getSupportFragmentManager())
                .setFragmentTabLayoutAdapter(new DefaultFragmentTabAdapter(Arrays.asList(fragmentClass), Arrays.asList(textViewArray), Arrays.asList(drawables)) {
                    @Override
                    public View createView(int pos) {
                        //Toast.makeText(MainActivity.this,pos,Toast.LENGTH_SHORT).show();
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, null);
                        ImageView imageView = view.findViewById(R.id.img);
                        imageView.setImageResource(drawables[pos]);
                        TextView textView = view.findViewById(R.id.tab_text);
                        textView.setText(textViewArray[pos]);
                        return view;
                    }

                    @Override
                    public void onClick(int pos) {

                        ALog.d(TAG, "onClick:" + pos);
                    }
                }).creat();

        //扫码添加设备 注册
        ScanManager.getInstance().registerPlugin(AddDeviceScanPlugin.NAME, new AddDeviceScanPlugin(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //需要取消注册，否则会造成内存泄露
        ScanManager.getInstance().unRegisterPlugin(AddDeviceScanPlugin.NAME);
//        //退出首页不显示浮窗
//        FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
//        if (helper != null) {
//            helper.setNeedShowFloatWindowFlag(false);
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Log.d(TAG, "onActivityResult");
            if (data != null && data.getStringExtra("productKey") != null) {
                Bundle bundle = new Bundle();
                bundle.putString("productKey", data.getStringExtra("productKey"));
                bundle.putString("deviceName", data.getStringExtra("deviceName"));
                bundle.putString("token", data.getStringExtra("token"));
                Intent intent = new Intent(this, BindAndUseActivity.class);
                intent.putExtras(bundle);
                this.startActivity(intent);
            }
        }
//        if (requestCode == 100) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.canDrawOverlays(this)) {
//                    FloatWindowHelper helper = FloatWindowHelper.getInstance(getApplication());
//                    if (helper != null) {
//                        helper.setNeedShowFloatWindowFlag(true);
//                    }
//                }
//            }
//        }

    }
}
