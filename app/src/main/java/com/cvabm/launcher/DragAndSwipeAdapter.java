package com.cvabm.launcher;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.dragswipe.listener.DragAndSwipeDataCallback;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;

public class DragAndSwipeAdapter extends BaseQuickAdapter<DefaultDragAndSwipeActivity.AppBean, QuickViewHolder> implements DragAndSwipeDataCallback {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        return new QuickViewHolder(R.layout.item_draggable_view, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int position, DefaultDragAndSwipeActivity.AppBean item) {

        holder.setImageDrawable(R.id.iv_head, item.getDrawable());
//        switch (holder.getLayoutPosition() % 3) {
//            holder.setImageResource(R.id.iv_head, R.mipmap.head_img0);
//            case 1 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img1);
//            case 2 -> holder.setImageResource(R.id.iv_head, R.mipmap.head_img2);
//            default -> {
//            }
//        }
//        holder.setText(R.id.tv, item);
    }

    @Override
    public void dataMove(int fromPosition, int toPosition) {
        move(fromPosition, toPosition);
    }

    @Override
    public void dataRemoveAt(int position) {
        removeAt(position);
    }
}
