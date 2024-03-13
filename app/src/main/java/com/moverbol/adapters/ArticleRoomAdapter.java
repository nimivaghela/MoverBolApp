package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ItemArticleRoomBinding;
import com.moverbol.interfaces.ArticleRoomSelectedListener;
import com.moverbol.model.confirmationPage.artialeList.ArticleRoomModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22-09-2017.
 */


public class ArticleRoomAdapter extends RecyclerView.Adapter<ArticleRoomAdapter.HomeItemHolder> {
    private ArrayList<ArticleRoomModel> homeList;
    private int selectedPosition = 0;
    private ArticleRoomSelectedListener articleRoomSelectedListener;

    public ArticleRoomAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArticleRoomAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_room, parent, false);
        return new ArticleRoomAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleRoomAdapter.HomeItemHolder holder, int position) {
        holder.itemArticleRoomBinding.setArticleRoomModel(homeList.get(position));
        holder.itemArticleRoomBinding.setIsSelected(selectedPosition == position);
        holder.itemArticleRoomBinding.executePendingBindings();

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

    public void addToHomeList(ArticleRoomModel articleRoomModel) {
        if (this.homeList != null) {
            homeList.add(articleRoomModel);
            notifyItemInserted(homeList.size() - 1);
        }
    }

    public List<ArticleRoomModel> getHomeList() {
        return homeList;
    }

    public void setHomeList(ArrayList<ArticleRoomModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public void setArticleRoomSelectedListener(ArticleRoomSelectedListener articleRoomSelectedListener) {
        this.articleRoomSelectedListener = articleRoomSelectedListener;
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        ItemArticleRoomBinding itemArticleRoomBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            itemArticleRoomBinding = DataBindingUtil.bind(itemView);
            if (itemArticleRoomBinding != null) {
                itemArticleRoomBinding.getRoot().setOnClickListener(v -> {
                    selectedPosition = getAdapterPosition();
                    articleRoomSelectedListener.selectedArticleRoom(homeList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                });
            }
        }
    }
}
