package com.moverbol;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.HomeBinding;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.HomeViewModel;
import com.moverbol.views.activities.BackgroundLocationActivity;
import com.moverbol.views.activities.SetPasswordActivity;
import com.moverbol.views.fragments.home.CalendarFragment;
import com.moverbol.views.fragments.home.HomeFragment;
import com.moverbol.views.fragments.home.NotificationFragment;
import com.moverbol.views.fragments.home.WeekCalendarFragment;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

public class HomeActivity extends BaseAppCompactActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String BACK_STACK_CALENDER_TAG = "calender_fragment";
    private static final String BACK_STACK_WEEK_TAG = "week_fragment";
    private static final String BACK_STACK_NOTIFICATION_TAG = "notification_fragment";
    public static final int RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR = 100;
    private final String NAVIGATION_ITEM = "navigation_item";
    private HomeBinding homeBinding;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager mFragmentManager;
    //@Constants.NavigationItems
    private int selectedFragment;
    private MoversPreferences moversPreferences;
    private HomeViewModel viewModel;

    private HomeFragment mHomeFragment;
    private CalendarFragment mCalendarFragment;
    private WeekCalendarFragment mWeekCalendarFragment;
    private NotificationFragment mNotificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mHomeFragment = new HomeFragment();
        mCalendarFragment = new CalendarFragment();
        mWeekCalendarFragment = new WeekCalendarFragment();
        mNotificationFragment = new NotificationFragment();
        mFragmentManager = getSupportFragmentManager();

        moversPreferences = MoversPreferences.getInstance(this);

        setSupportActionBar(homeBinding.appBarHome.toolbar);
        getSupportActionBar().setTitle(R.string.home);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, homeBinding.drawerLayout, homeBinding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        homeBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        homeBinding.navView.setNavigationItemSelectedListener(this);

        //openHomeFragment();
        if (savedInstanceState != null) {
            selectedFragment = savedInstanceState.getInt(NAVIGATION_ITEM);
        } else {
            selectedFragment = Constants.HOME;
            openNavigationFragment(selectedFragment);
        }

        homeBinding.navView.setCheckedItem(R.id.nav_home);
        View headerView = homeBinding.navView.getHeaderView(0);
        TextView user_name = headerView.findViewById(R.id.txtvw_user_name);
//        user_name.setText(moversPreferences.getUserDetails().getUserName());
        user_name.setText(moversPreferences.getUserName());

        TextView user_email_id = headerView.findViewById(R.id.txt_user_email_id);
//        user_email_id.setText(moversPreferences.getUserDetails().getUserEmailId());
        user_email_id.setText(moversPreferences.getUserEmail());

        ImageView user_profile_image = headerView.findViewById(R.id.iv_user);
        String userProfileImage = MoversPreferences.getInstance(this).getProfileImageUrl();
        if (!TextUtils.isEmpty(userProfileImage)) {
//            Picasso.with(this).load(userProfileImage).into(user_profile_image);
            Picasso.get().load(userProfileImage).into(user_profile_image);
        }


//        if(selectedFragment==Constants.WEEK_VIEW)
//        openNavigationFragment(selectedFragment);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeBinding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigationFragment(Constants.CALENDAR);
//                onNavigationItemSelected(homeBinding.navView.getMenu().getItem(Constants.CALENDAR));
//                homeBinding.navView.setCheckedItem(Constants.CALENDAR);
                homeBinding.navView.getMenu().getItem(Constants.CALENDAR).setChecked(true);
            }
        });

        setMenuCounter(MoversPreferences.getInstance(HomeActivity.this).getNotificationCount());

        if (!checkLocationPermission() && !moversPreferences.getUserDenyLocationPermission()) {
            startActivity(new Intent(this, BackgroundLocationActivity.class));
        }
    }

    public void setTittle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Useful for broadcast receiver. We have one broadcast for delete job which checks the
        // current job id and if it is same as the deleted job id then it shows an alert. We don't
        // want it to show that alert when we are on home screen.
        MoversPreferences.getInstance(this).setCurrentJobId("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAVIGATION_ITEM, selectedFragment);
    }

    @Override
    public void onBackPressed() {
        if (homeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
            if (fragment instanceof HomeFragment) {
                super.onBackPressed();
            } else {
                openHomeFragment();

                selectedFragment = Constants.HOME;
                setTittle(getString(R.string.home));
                homeBinding.navView.setCheckedItem(R.id.nav_home);
                homeBinding.appBarHome.fab.show();
//                homeBinding.appBarHome.fab.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        if (selectedFragment == Constants.HOME) {

        } else if (selectedFragment == Constants.CALENDAR) {
            getMenuInflater().inflate(R.menu.calendar, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_calendar) {
//            openCalendarViewFragment(Constants.CALENDAR_VIEW);
            openCalendarFragment();
        } else if (id == R.id.action_week) {
//            openCalendarViewFragment(Constants.WEEK_VIEW);
            openWeekFragment();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
//            openHomeFragment();
            openNavigationFragment(Constants.HOME);
            setTittle(getString(R.string.home));
        } else if (id == R.id.nav_calendar) {
            //openCalendarFragment();
            openNavigationFragment(Constants.CALENDAR);
            setTittle(getString(R.string.home));
        } else if (id == R.id.nav_logout) {
            showLogoutAlert();
        } else if (id == R.id.nav_jobs_in_progress) {
            openNavigationFragment(Constants.JOBS_IN_PROGRESS);
            setTittle(getString(R.string.home));
        } else if (id == R.id.nav_notifications) {
            openNavigationFragment(Constants.NOTIFICATIONS);
            setTittle(getString(R.string.notifications));
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SetPasswordActivity.class);
            intent.putExtra(Constants.EXTRA_CALLED_FROM_HOME, true);
            startActivity(intent);
        }

        homeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private void openCalendarViewFragment(int flag) {
        CalendarHolderFragment calendarFragment = (CalendarHolderFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.CALENDAR));
        calendarFragment.changeFragment(flag);
        homeBinding.appBarHome.fab.setVisibility(View.GONE);
        selectedFragment = flag;
    }*/

    private void openNavigationFragment(@Constants.NavigationItems int item) {
        fragmentTransaction = mFragmentManager.beginTransaction();
        invalidateOptionsMenu();
        if (Constants.HOME == item) {
//            fragmentTransaction.replace(homeBinding.appBarHome.clLayout.clHome.getId(), mHomeFragment, String.valueOf(Constants.HOME));
            openHomeFragment();
//            homeBinding.appBarHome.fab.setVisibility(View.VISIBLE);
            homeBinding.appBarHome.fab.show();
        } else if (Constants.CALENDAR == item) {
//            fragmentTransaction.replace(homeBinding.appBarHome.clLayout.clHome.getId(), new CalendarHolderFragment(), String.valueOf(Constants.CALENDAR));
            openCalendarFragment();
            homeBinding.appBarHome.fab.hide();
        } else if (Constants.JOBS_IN_PROGRESS == item) {
            openHomeFragmentForJobsInProgress();
        } else if (Constants.NOTIFICATIONS == item) {
            openNotificationFragment();
            homeBinding.appBarHome.fab.hide();
        }
//        fragmentTransaction.commit();
        selectedFragment = item;
    }

    private void openHomeFragment() {

        Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
        if (fragment != null && fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).removeAllJobFilters();
            return;
        }

        fragmentTransaction = mFragmentManager.beginTransaction();

        // Pop off everything up to and including the current tab
        mFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction
                .replace(homeBinding.appBarHome.clLayout.clHome.getId(), mHomeFragment)
                .commit();
        selectedFragment = Constants.HOME;
    }


    private void openNotificationFragment() {

        Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
        if (fragment != null && fragment instanceof NotificationFragment) {
            return;
        }

        fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(homeBinding.appBarHome.clLayout.clHome.getId(), mNotificationFragment);
        fragmentTransaction.commit();
        selectedFragment = Constants.NOTIFICATIONS;
    }

    private void openHomeFragmentForJobsInProgress() {
        Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).showOnlyJobsInProgress();
            return;
        }

        fragmentTransaction = mFragmentManager.beginTransaction();

        // Pop off everything up to and including the current tab
        mFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        // Add the new tab fragment
        fragmentTransaction
                .replace(homeBinding.appBarHome.clLayout.clHome.getId(), new HomeFragment())
                .commit();

        mHomeFragment.showOnlyJobsInProgress();

        selectedFragment = Constants.JOBS_IN_PROGRESS;
    }

    private void openCalendarFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
        if (fragment instanceof CalendarFragment) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(mCalendarFragment);
        fragmentTransaction.replace(homeBinding.appBarHome.clLayout.clHome.getId(), new CalendarFragment());
        fragmentTransaction.commit();
        selectedFragment = Constants.CALENDAR;
    }

    private void openWeekFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(homeBinding.appBarHome.clLayout.clHome.getId());
        if (fragment instanceof WeekCalendarFragment) {
            return;
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(mWeekCalendarFragment);
        fragmentTransaction.replace(homeBinding.appBarHome.clLayout.clHome.getId(), new WeekCalendarFragment());
        fragmentTransaction.commit();
        selectedFragment = Constants.WEEK_VIEW;
    }


    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
                .setMessage(R.string.logout_alert_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callLogout();
                    }
                })
                .setNegativeButton(getString(R.string.no_), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callLogout() {
        if (!shouldMakeNetworkCall(homeBinding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = moversPreferences.getSubdomain();
        String userId = moversPreferences.getUserId();

        viewModel.logout(subdomain, userId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
//                logoutDueToUnauthentication(false);
                Util.logoutDueToUnauthentication(HomeActivity.this, false);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(HomeActivity.this);
                    return;
                }

                Snackbar.make(homeBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(homeBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void setMenuCounter(int count) {
        TextView view = (TextView) homeBinding.navView.getMenu().findItem(R.id.nav_notifications).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    private boolean checkLocationPermission() {
        String[] permission;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {
            permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        }
        return Util.checkSelfPermission(this, permission);

    }

}
