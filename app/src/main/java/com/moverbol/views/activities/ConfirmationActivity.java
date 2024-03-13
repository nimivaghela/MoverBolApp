package com.moverbol.views.activities;

/*
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.moverbol.R;
import com.moverbol.adapters.HomePagerAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ConfirmationBinding;
import com.moverbol.interfaces.ConfirmationInterface;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;
import com.moverbol.views.activities.jobsummary.MoveProcessActivity;
import com.moverbol.views.fragments.confirmation.CrewFragment;
import com.moverbol.views.fragments.confirmation.MaterialFragment;
import com.moverbol.views.fragments.confirmation.TruckFragment;

*/
/**
 * Created by sumeet on 8/9/17.
 *//*


public class ConfirmationActivity extends BaseAppCompactActivity implements TabLayout.OnTabSelectedListener, ConfirmationInterface {

    private ConfirmationBinding confirmationBinding;
    private HomePagerAdapter adapter;
    private ConfirmationDetailsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmationBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirmation);

        viewModel = ViewModelProviders.of(this).get(ConfirmationDetailsViewModel.class);

        adapter = new HomePagerAdapter(getSupportFragmentManager());

        CrewFragment crewFragment = new CrewFragment();
        adapter.addFragment(crewFragment, getString(R.string.crew));
        TruckFragment truckFragment = new TruckFragment();
        adapter.addFragment(truckFragment, getString(R.string.trucks));
        MaterialFragment materialFragment = new MaterialFragment();
        adapter.addFragment(materialFragment, getString(R.string.materials));

        confirmationBinding.vpConfirmation.setAdapter(adapter);
        confirmationBinding.tabLayout.setupWithViewPager(confirmationBinding.vpConfirmation);
        confirmationBinding.tabLayout.addOnTabSelectedListener(this);

        onTabSelected(confirmationBinding.tabLayout.getTabAt(0));

        setToolbar(confirmationBinding.toolbar2, getString(R.string.confirmation), R.drawable.ic_arrow_back_white_24dp);

        confirmationBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDetail(v);
            }
        });

        if(viewModel.jobConfirmationLive.getValue()==null){
            getJobConfirmation();
        }

    }

    private void getJobConfirmation() {
        */
/*if(!shouldMakeNetworkCall(confirmationBinding.getRoot())){
            return;
        }
        if(getIntent()==null || getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY)==null){
            return;
        }
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String job_Id = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);

        showProgress();
        viewModel.setJobConfirmation(subdomain, userId, job_Id, new ResponseListener<BaseResponseModel<JobConfirmationPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobConfirmationPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Snackbar.make(confirmationBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
//                Util.showSnackBarWithNullPosibility(confirmationBinding.getRoot(), serverResponseError.getMessage(), getString(R.string.failed));
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobConfirmationPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(confirmationBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
//                Util.showSnackBarWithNullPosibility(confirmationBinding.getRoot(), t.getMessage(), getString(R.string.failed));
            }
        });*//*



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirmation_move_process, menu);

        //Trying to change color of menu items to while. Till now unsucessfull.
        MenuItem item = menu.findItem(R.id.action_move_process);
        SpannableString s = new SpannableString(getString(R.string.start_move));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_move_process) {
            Intent intent = new Intent(ConfirmationActivity.this, MoveProcessActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        showHideFAB(View.VISIBLE);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void showHideFAB(int visibility) {
        if (visibility == View.VISIBLE) {
            confirmationBinding.floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        } else if (visibility == View.INVISIBLE) {
            confirmationBinding.floatingActionButton.animate().translationY(confirmationBinding.floatingActionButton.getHeight() + 100).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    public void openAddDetail(View view) {
        Intent intent = null;
        if (confirmationBinding.tabLayout.getSelectedTabPosition() == 0) {
            intent = new Intent(this, AddCrewActivity.class);
        } else if (confirmationBinding.tabLayout.getSelectedTabPosition() == 1) {
            intent = new Intent(this, AddTruckActivity.class);
        } else if (confirmationBinding.tabLayout.getSelectedTabPosition() == 2) {
            intent = new Intent(this, AddMaterialActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
*/
