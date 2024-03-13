package com.moverbol.views.activities.jobsummary;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moverbol.R;
import com.moverbol.adapters.CommentAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.CommentListBinding;
import com.moverbol.model.CommentPojo;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 14/9/17.
 */

public class CommentListActivity extends BaseAppCompactActivity {

    private JobSummaryViewModel viewModel;
    private CommentAdapter adapter;
    private CommentListBinding commentListBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentListBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment_list);
        //setToolbar(crewListBinding.to);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);
        }

        commentListBinding.rvCrew.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        setToolbar(commentListBinding.toolbar.toolbar, getString(R.string.comments), R.drawable.ic_arrow_back_white_24dp);

        List<CommentPojo> commentsPojos = new ArrayList<>();
        adapter = new CommentAdapter(commentsPojos);
        commentListBinding.setAdapter(adapter);


        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {

                adapter.setCommentList(jobDetailPojo.getMoveStageDetailsPojoList().get(0).getComments());
            }
        });


        viewModel.setJobDetailLive();
        if (viewModel.getJobDetailLive().getValue() != null)
            adapter.setCommentList(viewModel.getJobDetailLive().getValue().getMoveStageDetailsPojoList().get(0).getComments());
    }

}
