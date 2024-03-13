package com.moverbol.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.CommentItemBinding;
import com.moverbol.model.CommentPojo;

import java.util.List;
//import com.moverbol.model.PickupStopPojo;

/**
 * Created by sumeet on 11/9/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.HomeItemHolder> {

    //    private ObservableArrayList<CommentPojo> commentList;
    private List<CommentPojo> commentList;

    public CommentAdapter(List<CommentPojo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new CommentAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.HomeItemHolder holder, int position) {
        holder.commentItemBinding.setCommentPojo(commentList.get(position));
        holder.commentItemBinding.setPosition(position + 1);
        if (commentList.get(position).getNotesType().equals("1")) {
            holder.commentItemBinding.txtNum.setTypeface(null, Typeface.BOLD);
            holder.commentItemBinding.txtDate.setTypeface(null, Typeface.BOLD);
            holder.commentItemBinding.txtDescription.setTypeface(null, Typeface.BOLD);
            holder.commentItemBinding.txtTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.commentItemBinding.txtNum.setTypeface(null, Typeface.NORMAL);
            holder.commentItemBinding.txtDate.setTypeface(null, Typeface.NORMAL);
            holder.commentItemBinding.txtDescription.setTypeface(null, Typeface.NORMAL);
            holder.commentItemBinding.txtTitle.setTypeface(null, Typeface.NORMAL);
        }
        holder.commentItemBinding.executePendingBindings();
    }

    public void setCommentList(List<CommentPojo> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (commentList == null) {
            return 0;
        } else {
            return commentList.size();
        }
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        CommentItemBinding commentItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            commentItemBinding = DataBindingUtil.bind(itemView);

//            SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy");
//            commentItemBinding.setDate_format(format);
        }
    }

}
