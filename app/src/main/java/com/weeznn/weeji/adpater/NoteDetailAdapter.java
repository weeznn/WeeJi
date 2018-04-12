package com.weeznn.weeji.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by weeznn on 2018/4/12.
 */

public class NoteDetailAdapter extends RecyclerView.Adapter<NoteDetailAdapter.NoteDetailViewHolder> {

    // TODO: 2018/4/12  写好adapter
    @Override
    public NoteDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(NoteDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NoteDetailViewHolder extends RecyclerView.ViewHolder {

        public NoteDetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
