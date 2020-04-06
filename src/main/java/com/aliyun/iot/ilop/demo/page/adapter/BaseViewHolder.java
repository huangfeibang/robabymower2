package com.aliyun.iot.ilop.demo.page.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {




    public BaseViewHolder(View itemView) {
        super(itemView);
    }




    public void onBind(T data,int position){

    }


}
