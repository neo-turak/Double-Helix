/*
 * Copyright (c) 2018.
 * Developed by Turak Nurahmat
 * Under the guide of  the University of Hertfordshire, Master of Computer Science final project team.
 * SID:15066679
 * License: Apache 2.0
 * All rights reserved!
 */

package cn.nurasoft.doublehelix.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.nurasoft.doublehelix.R;

/**
 * Created by Miro: on 2017/6/28.
 **/
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private ArrayList<ItemModel> dataList = new ArrayList<>();

    public void replaceAll(ArrayList<ItemModel> list) {

        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemModel> list) {

        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.CHAT_A:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_a, parent, false));
            case ItemModel.CHAT_B:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_b, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class BaseAdapter extends RecyclerView.ViewHolder {

        BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        ChatAViewHolder(View view) {
            super(view);
            ic_user = itemView.findViewById(R.id.ic_user);
            tv = itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            tv.setText(model.getContent());
        }
    }

    private class ChatBViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        ChatBViewHolder(View view) {
            super(view);
            ic_user = itemView.findViewById(R.id.ic_user);
            tv = itemView.findViewById(R.id.tv);

        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Picasso.with(itemView.getContext()).load(model.getIcon()).placeholder(R.mipmap.ic_launcher).into(ic_user);
            tv.setText(model.getContent());
        }
    }
}
