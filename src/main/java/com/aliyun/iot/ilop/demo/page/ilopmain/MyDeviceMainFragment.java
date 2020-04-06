package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aliyun.iot.demo.R;
import com.aliyun.iot.ilop.demo.dialog.ASlideDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyDeviceMainFragment extends Fragment {
    private View myInfoView;

    private View myAboutView;

    private View myOTAView;
    private View mMessgae;

    private TextView myUserNameTV;

    // account
    ASlideDialog menuDialog;

    private MediaPlayer music;
    private int soundID;
    private SoundPool soundPool;

    private Button mBtnMoveNow;
    private Button mBtnAutoCharge;
    private Button mBtnMap;
    private Button mBtnBooking;
    private Button mBtnManual;

    private Button mBtnHome;
    private Button mBtnDevice;
    private Button mBtnFeedback;
    private Button mBtnUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreateView");
        return inflater.inflate(R.layout.fragment_device_main, null);


    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSound();
        WaitingFragment framents = new WaitingFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.middle,framents).commitAllowingStateLoss();

        mBtnMoveNow = view.findViewById(R.id.btn_move);
        mBtnMoveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnMove();
            }
        });
        mBtnAutoCharge =  view.findViewById(R.id.btn_auto);
        mBtnAutoCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAuto();
            }

        });
        mBtnMap = view.findViewById(R.id.btn_map);
        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnMap();

            }

        });
        mBtnBooking = view.findViewById(R.id.btn_booking);
        mBtnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBooking();
            }
        });
        mBtnManual = view.findViewById(R.id.btn_manual);
        mBtnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnManual();
            }
        });

        /*
        myInfoView = view.findViewById(R.id.my_userinfo);
        myAboutView = view.findViewById(R.id.my_about);
        myUserNameTV = view.findViewById(R.id.my_username_textview);
        myOTAView = view.findViewById(R.id.my_ota);
        mMessgae = view.findViewById(R.id.my_message);

        myAboutView.setOnClickListener(v -> Router.getInstance().toUrl(getActivity(), "page/about"));

        myOTAView.setOnClickListener(v -> Router.getInstance().toUrl(getActivity(), "page/ota/list"));

        mMessgae.setOnClickListener(v -> {
            //调用消息插件

            String code = "link://router/devicenotices";
            Bundle bundle = new Bundle();
            Router.getInstance().toUrlForResult(getActivity(), code, 1, bundle);
        });


        myInfoView.setOnClickListener(v -> {
            if (!LoginBusiness.isLogin()) {
                LoginBusiness.login(new ILoginCallback() {
                    @Override
                    public void onLoginSuccess() {
                        myUserNameTV.setText(getUserNick());
                    }

                    @Override
                    public void onLoginFailed(int i, String s) {
                        Toast.makeText(getActivity(), getString(R.string.account_login_failed), Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            accountShowMenuDialog();
        });
        String userName = getUserNick();
        myUserNameTV.setText(userName);

        view.findViewById(R.id.my_language).setOnClickListener(view1 -> LanguageSettingActivity.start(view1.getContext()));

         */
    }

    private void OnMove(){
        playSound();
        Toast.makeText(getActivity(),"Moving", Toast.LENGTH_SHORT).show();
    }

    private void OnAuto(){
        playSound();
        Toast.makeText(getActivity(),"autocharge",Toast.LENGTH_SHORT).show();
    }

    private void OnMap() {
        playSound();
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for(int i = 0; i< fragments.size();i++)
        {
            getChildFragmentManager().beginTransaction().remove(fragments.get(i));
        }

        MapFragment framents = new MapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.middle,framents).commitAllowingStateLoss();
    }

    private void OnBooking(){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        playSound();
        TimePickerDialog pikerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final String[] weekday = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
                boolean[] begin = new boolean[]{false,false,false,false,false,false,false};
                final List<String> choose = new ArrayList<>();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle("Please select a week");
                builder.setMultiChoiceItems(weekday, begin, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            choose.add(weekday[which]);
                        }else{
                            choose.remove(weekday[which]);
                        }

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "You chooce"+choose.toString()+"to start move", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                Toast.makeText(getActivity(), "Will start at "+hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
            }
        }, hourOfDay, minute, true);
        pikerDialog.show();
    }

    private void OnManual(){
        playSound();
        final EditText et = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("Password")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String passwd = et.getText().toString();
                        if(passwd.equals("0000")){
                            OperatorFragment framents = new OperatorFragment();
                            getChildFragmentManager().beginTransaction().replace(R.id.middle,framents).commitAllowingStateLoss();
                        }
                        else{
                            Toast.makeText(getContext(),"Password error",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("cancel",null)
                .show();
    }

    private void PlayMusic(int MusicId) {
        music = MediaPlayer.create(getContext(), MusicId);
        music.start();
    }
    @SuppressLint("NewApi")
    private void initSound() {
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(getContext(), R.raw.jianpan, 1);
    }
    private void playSound() {
        soundPool.play(
                soundID,
                0.5f,      //左耳道音量【0~1】
                0.5f,      //右耳道音量【0~1】
                0,         //播放优先级【0表示最低优先级】
                0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("onResume");
        WaitingFragment framents = new WaitingFragment();
        List<Fragment> fragmentss = getChildFragmentManager().getFragments();
        for(int i = 0; i< fragmentss.size();i++)
        {
            getChildFragmentManager().beginTransaction().remove(fragmentss.get(i));
        }
        getChildFragmentManager().beginTransaction().add(R.id.middle,framents).commitAllowingStateLoss();

    }


    @Override
    public void onPause(){
        super.onPause();

        List<Fragment> fragmentss = getChildFragmentManager().getFragments();
        WaitingFragment framents = new WaitingFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.middle,framents).commitAllowingStateLoss();
    }

}
