package com.moverbol.util;

/**
 * Created by AkashM on 8/1/18.
 */

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.moverbol.R;

import java.util.ArrayList;
import java.util.List;


public class PermissionHelper {


    private static final String TAG = "PermissionHelper";
    private static int REQUEST_CODE;

    /*private Activity activity;
    private Fragment fragment;*/
    private final Object object;
    private final String[] permissions;
    private PermissionCallback mPermissionCallback;
    private boolean showRational;


    /*   //=========Constructors - START=========
       public PermissionHelper(Activity activity, Fragment fragment, String[] permissions, int requestCode) {
           this.activity = activity;
           this.fragment = fragment;
           this.permissions = permissions;
           this.REQUEST_CODE = requestCode;
           checkIfPermissionPresentInAndroidManifest();
       }

       public PermissionHelper(Activity activity, String[] permissions, int requestCode) {
           this.activity = activity;
           this.permissions = permissions;
           this.REQUEST_CODE = requestCode;
           checkIfPermissionPresentInAndroidManifest();
       }*/
    private final String rationalMessage;
    private AlertDialog dialog;

    /**
     * @param object          Activity Fragment
     * @param permissions     Requested permissions
     * @param requestCode     Constant request code
     * @param rationalMessage message
     */
    public PermissionHelper(Object object, String[] permissions, int requestCode, String rationalMessage) {
        this.object = object;
        this.permissions = permissions;
        REQUEST_CODE = requestCode;
        this.rationalMessage = rationalMessage;

        checkIfPermissionPresentInAndroidManifest();
    }

    private Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    private void showRationaleDialog() {
        if (showRational) {
            if (!TextUtils.isEmpty(rationalMessage)) {
                dialog = new AlertDialog.Builder(this.getContext(), R.style.AppTheme_AlertDialogTheme)
                        .setMessage(TAG)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog.dismiss();
                                mPermissionCallback.onPermissionDeniedBySystem();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                // act as if the permissions were denied
                                mPermissionCallback.onPermissionDenied();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.setMessage(rationalMessage);
                dialog.show();
            }
        }
    }

    /**
     *
     * @param messageOnDialog like "You have denied permission required for this feature. To access this change permission settings."
     * @param messageOnSettingToast like "Go to permissions and enable camera permission"
     */
    public void showGoToSettingsDialog(@NonNull String messageOnDialog,@NonNull final String messageOnSettingToast) {
        final Context context = this.getContext();
        if (!TextUtils.isEmpty(rationalMessage)) {
            dialog = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialogTheme)
                    .setMessage(TAG)
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);
                                Toast.makeText(context, messageOnSettingToast, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            // act as if the permissions were denied
                            mPermissionCallback.onPermissionDenied();
                            dialog.dismiss();
                        }
                    }).create();
            dialog.setMessage(messageOnDialog);
            dialog.show();
        }
    }

    private void checkIfPermissionPresentInAndroidManifest() {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                throw new RuntimeException("Permission (" + permission + ") Not Declared in manifest");
            }
        }
    }

    //====================================
    //====================================

    //=========Constructors- END=========
    public void request(PermissionCallback permissionCallback) {
        this.mPermissionCallback = permissionCallback;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionCallback.onPermissionGranted();
        } else {
            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (!checkSelfPermission(permissions)) {
            showRational = shouldShowRational(permissions);
            if (object instanceof Activity) {
                ActivityCompat.requestPermissions(((Activity) object), filterNotGrantedPermission(permissions), REQUEST_CODE);
            } else {
                ((Fragment) object).requestPermissions(filterNotGrantedPermission(permissions), REQUEST_CODE);
            }

        } else {
            Log.i(TAG, "PERMISSION: Permission Granted");
            if (mPermissionCallback != null)
                mPermissionCallback.onPermissionGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            boolean denied = false;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    denied = true;
                    break;
                }
            }

            if (denied) {
                boolean currentShowRational = shouldShowRational(permissions);
                if (!showRational && !currentShowRational) {
                    Log.d(TAG, "PERMISSION: Permission Denied By System");
                    if (mPermissionCallback != null)
                        mPermissionCallback.onPermissionDeniedBySystem();
                } else {
                    Log.i(TAG, "PERMISSION: Permission Denied");
                    if (!showRational) {
                        if (mPermissionCallback != null)
                            mPermissionCallback.onPermissionDenied();
                    } else {
                        showRationaleDialog();
                    }
                }
            } else {
                Log.i(TAG, "PERMISSION: Permission Granted");
                if (mPermissionCallback != null)
                    mPermissionCallback.onPermissionGranted();
            }
        }
    }

    private <T extends Context> T getContext() {
        /*if (activity != null)
            return (T) activity;
        return (T) fragment.getContext();*/

        if (object instanceof Activity) {
            return (T) object;
        } else if (object instanceof Fragment) {
            return (T) ((Fragment) object).getContext();
        } else if (object instanceof android.app.Fragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (T) ((android.app.Fragment) object).getContext();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Return list that is not granted and we need to ask for permission
     *
     * @param permissions
     * @return
     */
    private String[] filterNotGrantedPermission(String[] permissions) {
        List<String> notGrantedPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermission.add(permission);
            }
        }
        return notGrantedPermission.toArray(new String[notGrantedPermission.size()]);
    }

    /**
     * Check permission is there or not for group of permissions
     *
     * @param permissions
     * @return
     */
    public boolean checkSelfPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checking if there is need to show rational for group of permissions
     *
     * @param permissions
     * @return
     */
    private boolean shouldShowRational(String[] permissions) {
        boolean currentShowRational = false;
        for (String permission : permissions) {

            if (getActivity(object) != null) {
                if (shouldShowRequestPermissionRationale(getActivity(object), permission)) {
                    currentShowRational = true;
                    break;
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity(object).shouldShowRequestPermissionRationale(permission)) {
                        currentShowRational = true;
                        break;
                    }
                }
            }
        }
        return currentShowRational;
    }

    //===================
    public boolean hasPermission(String permission) {
        try {
            //Context context = activity != null ? activity : fragment.getActivity();
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface PermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();

        void onPermissionDeniedBySystem();
    }
}



