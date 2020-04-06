package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.iot.demo.R;

public class WaitingFragment extends Fragment {
    private MediaPlayer music;
    private int soundID;
    private SoundPool soundPool;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pending_fragment, container, false);
    }

}
