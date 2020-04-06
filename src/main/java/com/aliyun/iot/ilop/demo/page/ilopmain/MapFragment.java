package com.aliyun.iot.ilop.demo.page.ilopmain;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

import com.aliyun.iot.demo.R;

public class MapFragment extends Fragment {

    private MapViewModel mViewModel;
    private int count = 0;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (count <= 30){
                    /*执行一段逻辑*/
                    // add your code;
                    ImageView image = ((ImageView) view.findViewById(R.id.robot));;
                    AbsoluteLayout.LayoutParams params = (AbsoluteLayout.LayoutParams) image.getLayoutParams();
                    params.x = params.x -20;
                    image.setLayoutParams(params);
                    handler.postDelayed(this,500);//延时五百毫秒，再次执行这个runnable，如果isRegister为false了就停止执行了
                    count += 1;
                }
            }
        };

        //然后在你初次调起这个延时逻辑的地方调用以下语句：
        handler.postDelayed(runnable,500);//延时五百毫秒，执行runnable
    }


}
