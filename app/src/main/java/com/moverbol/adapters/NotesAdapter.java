package com.moverbol.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.NotesItemBinding;
import com.moverbol.model.notes.NotesPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 26-09-2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.HomeItemHolder> {
    private List<NotesPojo> homeList;
    //private String type;

    public NotesAdapter() {
        this.homeList = new ArrayList<>();
    }

    @Override
    public NotesAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notes_item, parent, false);
        return new NotesAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.HomeItemHolder holder, int position) {
        holder.notesItemBinding.setNotesPojo(homeList.get(position));
        if (homeList.get(position).getNotesType().equals("1")) {
            holder.notesItemBinding.txtDate.setTypeface(null, Typeface.BOLD);
            holder.notesItemBinding.txtDescription.setTypeface(null, Typeface.BOLD);
            holder.notesItemBinding.txtTitle.setTypeface(null, Typeface.BOLD);
        } else {
            holder.notesItemBinding.txtDate.setTypeface(null, Typeface.NORMAL);
            holder.notesItemBinding.txtDescription.setTypeface(null, Typeface.NORMAL);
            holder.notesItemBinding.txtTitle.setTypeface(null, Typeface.NORMAL);
        }
        holder.notesItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }

    public void setHomeList(List<NotesPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public NotesItemBinding notesItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            notesItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}

