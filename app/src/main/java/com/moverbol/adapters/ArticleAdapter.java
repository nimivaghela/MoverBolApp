package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ArticleItemBinding;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22-09-2017.
 */


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.HomeItemHolder> {
    private List<ArticleListItemPojo> homeList;

    public ArticleAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArticleAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_articles_item, parent, false);
        return new ArticleAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.HomeItemHolder holder, int position) {
        homeList.get(position).setActualQty();

        holder.articleItemBinding.setObj(homeList.get(position));
        holder.articleItemBinding.setPosition(position);
        holder.articleItemBinding.executePendingBindings();

        /*if(homeList.get(position).getBolStatus().equals("2")){
            holder.articleItemBinding.getRoot().setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }

    public void addToHomeList(ArticleListItemPojo articleListItemPojo) {
        if (this.homeList != null) {
            homeList.add(articleListItemPojo);
            notifyItemInserted(homeList.size() - 1);
        }
    }

    public List<ArticleListItemPojo> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<ArticleListItemPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        ArticleItemBinding articleItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            articleItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
