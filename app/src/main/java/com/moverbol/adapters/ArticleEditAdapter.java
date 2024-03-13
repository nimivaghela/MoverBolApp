package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.ArticleEditItemBinding;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;

import java.util.ArrayList;

/**
 * Created by Admin on 23-09-2017.
 */

public class ArticleEditAdapter extends RecyclerView.Adapter<ArticleEditAdapter.HomeItemHolder>{
    private ArrayList<ArticleListItemPojo> homeList;
    /*private OnDeleteClickedListener onDeleteClickedListener;

    public ArticleEditAdapter(OnDeleteClickedListener onDeleteClickedListener) {
        this.homeList = new ArrayList<>();
        this.onDeleteClickedListener = onDeleteClickedListener;
    }

    public interface OnDeleteClickedListener{
        void onDeleteClicked(int adapterPosition);
    }*/

    public ArticleEditAdapter() {
        this.homeList = new ArrayList<>();
    }

    @Override
    public ArticleEditAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article_edit_item, parent, false);
        return new ArticleEditAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleEditAdapter.HomeItemHolder holder, int position) {
        homeList.get(position).setActualQty();
        holder.articleEditItemBinding.setArticleeditPojo(homeList.get(position));
        holder.articleEditItemBinding.setPosition(position);
        /*holder.homeRvRowItemBinding.setHomeListAdapter(this);*/
        holder.articleEditItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }

    public void setHomeList(ArrayList<ArticleListItemPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public ArrayList<ArticleListItemPojo> getHomeList() {
        return homeList;
    }

    public void removeItem(int listItemPosition){
        this.homeList.remove(listItemPosition);
        notifyItemRemoved(listItemPosition);
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public ArticleEditItemBinding articleEditItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            articleEditItemBinding = DataBindingUtil.bind(itemView);

            /*articleEditItemBinding.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(articleEditItemBinding.getArticleeditPojo()!=null){
                        articleEditItemBinding.getArticleeditPojo().setBolStatus("2");
                    }
                    onDeleteClickedListener.onDeleteClicked(getAdapterPosition());
                }
            });*/
        }
    }

}
