package com.aliyun.iot.ilop.demo.page.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {


    protected List<T>mDatas;
    protected Context context;
    protected LayoutInflater inflater;

    public BaseRecycleViewAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
        mDatas=new ArrayList<>();
    }


    public void addData(int position,T t){
         mDatas.add(position,t);
         notifyItemInserted(position);
    }


    public void setData(int position,T t){
        mDatas.set(position,t);
        notifyItemChanged(position);
    }


    public void setDatas(List<T>datas){
      mDatas.clear();
      notifyDataSetChanged();
        addDatas(datas);
    }

    public void addDatas(List<T>datas){
        mDatas.addAll(datas);
        notifyItemRangeInserted(0,datas.size());
    }




    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
