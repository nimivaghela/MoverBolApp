package com.moverbol.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import androidx.databinding.ObservableArrayList;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.WorkManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.iceteck.silicompressorr.SiliCompressor;
import com.instabug.library.Instabug;
import com.moverbol.BuildConfig;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.database.MoverDatabase;
import com.moverbol.model.FileModel;
import com.moverbol.network.RetrofitClient;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.services.AlarmBroadcastReceiver;
import com.moverbol.views.activities.LoginActivity;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Mitesh on 21-07-2017.
 */

public class Util {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param view      to pass in snackbar.make
     * @param msg       to pass in snackbar.make
     * @param msgIfNull message to show if original message is null.
     */
    public static void showSnackBarWithNullPosibility(View view, String msg, String msgIfNull) {
        String message;
        if (msg == null || TextUtils.isEmpty(msg)) {
            message = msgIfNull;
        } else {
            message = msg;
        }
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBarWithAction(View view, String msg, String actionMsg, View.OnClickListener onClickListener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction(actionMsg, onClickListener).show();
    }

    public static void showLog(String tag, String msg) {
        if (BuildConfig.showLog && msg != null) {
            Log.d("###" + tag, msg);
        }
    }

    public static <String> ObservableArrayList<String> twoDArrayToList(String[][] twoDArray) {
        ObservableArrayList<String> list = new ObservableArrayList<>();
        for (String[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public static final int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }

    public static String rgbToHex(String rgbColor) {
        String[] ARGB = rgbColor.split(",");
        int r = Integer.parseInt(ARGB[0]);
        int g = Integer.parseInt(ARGB[1]);
        int b = Integer.parseInt(ARGB[2]);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static void slide_down(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void checkRequestContent(Request request) {
        Headers requestHeaders = request.headers();
        RequestBody requestBody = request.body();
        HttpUrl requestUrl = request.url();

        // todo make decision depending on request content
    }

    public static boolean isConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // assert cm != null;
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Parses API Server Errors into a POJO class.
     *
     * @param response the server responce
     * @return BaseResponseModel object.
     */
    public static BaseResponseModel parseError(Response<?> response) {
        Converter<ResponseBody, BaseResponseModel> converter =
                RetrofitClient.getRetrofit()
                        .responseBodyConverter(BaseResponseModel.class, new Annotation[0]);

        BaseResponseModel error;

        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            BaseResponseModel baseResponseModel = new BaseResponseModel();
            baseResponseModel.setMessage(Constants.RESPONSE_FAILURE_MESSAGE);
            return baseResponseModel;
        }

        if (error.getMessage() == null || TextUtils.isEmpty(error.getMessage()) || error.getMessage().equals("null") || error.getMessage().toLowerCase().contains("SQL".toLowerCase())) {
            error.setMessage(Constants.RESPONSE_FAILURE_MESSAGE);
        }

        return error;
    }


    /**
     * Returns formated date string
     *
     * @param dateString       The date string you want to format
     * @param inputDateFormat  Format pattern of input dateString.
     * @param outputDateFormat Desired output format pattern of input dateString.
     */
    public static String getFormattedDate(String dateString, String inputDateFormat, String outputDateFormat) {

        SimpleDateFormat oldFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());

        try {
            Date date = oldFormat.parse(dateString);
            SimpleDateFormat formatToTest = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
            Date dateToCompare = formatToTest.parse("01-01-1968");
            if (date.before(dateToCompare)) {
                return "";
            }
            return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }


    public static String getFormattedDate(Date date, String outputDateFormat) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(date);

    }

    public static String getFormattedTimeFromMillis(long timeInMillis, String outputDateFormat) {
        return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(new Date(timeInMillis));

    }


    public static String getFormattedCurrentDate(String outputDateFormat) {
        Date date = new Date();
        return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(date);
    }


    public static String getTimeFormattedStringFromMillis(long millisUntilFinished1) {

        long seconds = millisUntilFinished1 / 1000;
        long minute = seconds / 60;
        long hours = minute / 60;
        long days = hours / 24;

        long secondsToShow = seconds % 60;
        long minuteToShow = minute % 60;
        long hoursToShow = hours % 24;
        /*
        return TimeUnit.MILLISECONDS.toDays(millisUntilFinished1) + " "
                + TimeUnit.MILLISECONDS.toHours(millisUntilFinished1) + ":"
                + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished1) + ":"
                + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished1);*/
        NumberFormat f = new DecimalFormat("00");


        return /*f.format(days) + " " + */f.format(hoursToShow) + ":" + f.format(minuteToShow); /*+ ":" + f.format(secondsToShow);*/
    }

    public static String getTimeFormatedStringFromSeconds(long seconds) {
        return getTimeFormattedStringFromMillis(seconds * 1000);
    }


    public static Date getDateObjectFromStringDate(String date, String inputDateFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
        return dateFormat.parse(date);
    }

    public static boolean checkLocationEnabled(final Context context) {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialogTheme);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
            return false;
        }
        return true;
    }

    public static String getBase64EncodeStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public static String getBase64EncodeStringFromBitmap(File imageFile) {

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public static String getBase64EncodeStringFromBitmap(Context context, String imageUriString) throws IOException {

        Bitmap compressedImageBitmap = SiliCompressor.with(context).getCompressBitmap(imageUriString, true);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }


    public static void logoutDueToUnauthentication(Context context, boolean shouldShowMessage) {
        MoversPreferences.getInstance(context).loggedOut();
        MoverDatabase.getInstance(context).clearAllTables();
        Instabug.clearAllUserAttributes();
        Instabug.logoutUser();


        //Cancel Wor Manager works. In our case there is only one work. Periodic Location Update.
        WorkManager.getInstance().cancelAllWork();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (shouldShowMessage) {
            Toast.makeText(context, "Hey your session has expired! Looks like you logged in from another device", Toast.LENGTH_LONG).show();
        }

        //Remove all notifications.
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }

        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
        am.cancel(PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, 0));

    }


    public static void showLoginErrorDialog(final Context context) {
        try {
            MoversPreferences.getInstance(context).setLogin(false);

            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialogTheme);
            dialog.setTitle(context.getResources().getString(R.string.login_error_title));
            dialog.setMessage(context.getResources().getString(R.string.login_error_message));
            dialog.setPositiveButton(context.getResources().getString(R.string.go_to_login_screen), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    logoutDueToUnauthentication(context, false);

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
            dialog.setCancelable(false);
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static long getAvilableHeapMemory() {

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();

        long usedMemory = runtime.totalMemory() - runtime.freeMemory();

        return maxMemory - usedMemory;
    }

    public static String getFormatedPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        } else if (phoneNumber.length() < 6) {
            return phoneNumber;
        } else if (!TextUtils.isDigitsOnly(phoneNumber)) {
            return phoneNumber;
        }

        /*double phoneNum = 0;

        try {
            phoneNum = Double.parseDouble(phoneNumber);
        }catch (NumberFormatException e){
            return "";
        }

        DecimalFormat decimalFormat = new DecimalFormat(Constants.StringFormats.phoneNumberFormat);*/

        String bracketedCode = "(" + phoneNumber.substring(0, 3) + ") ";

        String midlePartWithDashAtEnd = phoneNumber.substring(3, 6) + "-";

        String lastDigits = phoneNumber.substring(6);

        return bracketedCode + midlePartWithDashAtEnd + lastDigits;
    }

    public static double getFormattedDouble(double doubleToFormat, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);

        return Double.parseDouble(decimalFormat.format(doubleToFormat));
    }

    public static double getFormattedDouble(String doubleToFormat, String format) {
        if (!NumberUtils.isNumber(doubleToFormat)) {
            return 0.00;
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);

        return Double.parseDouble(decimalFormat.format(doubleToFormat));
    }

    public static String getFormattedDoubleString(double doubleToFormat, String decimalFormatText, String stringFormatText) {
        /*if(!NumberUtils.isNumber(doubleToFormat)){
            return "";
        }*/
        DecimalFormat decimalFormat = new DecimalFormat(decimalFormatText);
        double d = Double.parseDouble(decimalFormat.format(doubleToFormat));
        float f = (float) d;
        String s = String.format(stringFormatText, f);
        return s;
    }

    public static String getFormattedDoubleString(String doubleToFormatString, String decimalFormatText, String stringFormatText) {
        if (!NumberUtils.isNumber(doubleToFormatString)) {
            return "";
        }
        double doubleToFormat = Double.parseDouble(doubleToFormatString);

        DecimalFormat decimalFormat = new DecimalFormat(decimalFormatText);
        double d = Double.parseDouble(decimalFormat.format(doubleToFormat));
        float f = (float) d;
        String s = String.format(stringFormatText, f);
        return s;
    }

    public static String getGeneralFormattedDecimalString(double doubleToFormat) {
//        doubleToFormat =  roundUpDoubleTo2DecimalPlaces(doubleToFormat);

        String doubleDecimalString = getFormattedDoubleString(doubleToFormat, Constants.DoubleFormats.FORMAT_FOR_DIGITS, Constants.DoubleFormats.STRING_FORMAT_FOR_DIGITS);
        return getFormatAmount(doubleDecimalString);
    }

    public static String getGeneralFormattedDecimalString(String doubleToFormat) {
        double d = 0.00;
        if (NumberUtils.isNumber(doubleToFormat)) {
            d = Double.parseDouble(doubleToFormat);
        } else {
            return doubleToFormat;
        }

        return getFormatAmount(getFormattedDoubleString(d, Constants.DoubleFormats.FORMAT_FOR_DIGITS, Constants.DoubleFormats.STRING_FORMAT_FOR_DIGITS));
    }

    public static double getDoubleFromString(String textToConvert) {
        try {
            return Double.parseDouble(textToConvert);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static long getLongFromString(String textToConvert) {
        try {
            return Long.parseLong(textToConvert);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getLongMillisFromHours(String hoursString) {
        double hours = getDoubleFromString(hoursString);

        double millisInDouble = (hours * 60 * 60 * 1000);

        return (long) millisInDouble;
    }

    public static double roundUpDoubleTo2DecimalPlaces(double numToRoundUp) {
        BigDecimal bd = new BigDecimal(numToRoundUp);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static double getRoundUpDouble(double numToRoundUp) {
       /* BigDecimal bd = new BigDecimal(numToRoundUp);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return (long) bd.doubleValue();*/
        String num = getGeneralFormattedDecimalString(numToRoundUp);
        return getDoubleFromString(removeFormatAmount(num));
    }

    public static void startDialer(Context context, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Could not find any application to dial this number.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static long getMillisFromSecondsString(String secondsString) {
        long seconds = Util.getLongFromString(secondsString);
        return (seconds * 1000);
    }


    public static long getRoundedUpTimeInMillis(long timeToRound) {
        long seconds = timeToRound / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long leftMinutes = minutes % 60;

        /*if(leftMinutes>44){
            if (leftMinutes > 52) {
                leftMinutes = 60;
            } else {
                leftMinutes = 45;
            }
        } else if(leftMinutes>29){
            if (leftMinutes > 37) {
                leftMinutes = 45;
            } else {
                leftMinutes = 30;
            }
        } else if(leftMinutes>14){
            if (leftMinutes > 22) {
                leftMinutes = 30;
            } else {
                leftMinutes = 15;
            }
        } else if(leftMinutes>0){
            if (leftMinutes > 7) {
                leftMinutes = 15;
            } else {
                leftMinutes = 0;
            }
        }*/

        if (leftMinutes > 45) {
            leftMinutes = 60;
        } else if (leftMinutes > 30) {
            leftMinutes = 45;
        } else if (leftMinutes > 15) {
            leftMinutes = 30;
        } else if (leftMinutes > 0) {
            leftMinutes = 15;
        }

        long minutesToReturn = (hours * 60) + leftMinutes;
        long secondsToReturn = minutesToReturn * 60;

        return (secondsToReturn * 1000);
    }

    public static double setMinuteInterval(long time, double maxHours, long minIncrementValue) {
        double finalTime = time;
        try {

            double seconds = time / 1000;
            double minutes = seconds / 60;
            double hours = minutes / 60;
            minutes = minutes % 60;
            double totalCount = 60 / minIncrementValue;

            Log.e("###", "hours : " + hours + " minutes : " + minutes);


            if (hours > maxHours) {
                if (minutes < minIncrementValue) {
                    minutes = minIncrementValue;
                } else {
                    for (int i = 1; i <= totalCount; i++) {
                        double interval = minIncrementValue * i; //change gap of second like 15, 30, 45, 60
                        if (interval > minutes) {
                            minutes = interval;
                            break;
                        }
                    }
                }
                finalTime = ((long) hours * 60 * 60 * 1000) + (minutes * 60 * 1000);
                Log.e("###", "minute Add" + minutes);
            } else {
                finalTime = maxHours * 60 * 60 * 1000;
            }

        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), Objects.requireNonNull(e.getLocalizedMessage()));
        }
        return finalTime;
    }

    public static String getRoundedUpNumericStringFromDoubleString(String numericString) {
        /*if(TextUtils.isEmpty(numericString) || !TextUtils.isDigitsOnly(numericString)){
            return "";
        }*/

        int numToReturn = (int) getDoubleFromString(numericString);

        return numToReturn + "";
    }

    public static void openMapForGivenAddress(String address, Context context) {

        if (address == null) {
            address = "";
        }

        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        context.startActivity(intent);
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    public static RequestBody getRequestBody(String description) {
        RequestBody requestBody = null;
        if (description != null) {
            requestBody = RequestBody.create(okhttp3.MultipartBody.FORM, description);
        }
        return requestBody;
    }

    public static File BitmapToFile(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".png");
            try {
                boolean isFile = f.createNewFile();
                if (isFile) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapData = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapData);
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return f;
        }
        return null;
    }


    public static MultipartBody.Part prepareFilePart(String partName, URI uri) {
        MultipartBody.Part multiPartBody = null;
        if (uri != null) {
            File file = new File(uri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            multiPartBody = MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }
        return multiPartBody;
    }

    public static void getSignature(PackageManager packageManager, String packageName) {
        Util.showLog("###", packageName);
        PackageInfo info;

        try {
            info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("### hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public static String convertCalenderToFormat(Calendar calendar, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    public static Calendar dateToCalender(String dateString, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            Date mDate = simpleDateFormat.parse(dateString);
            cal.setTime(mDate);
        } catch (Exception e) {
            e.printStackTrace();
            Util.showLog(Util.class.getSimpleName(), e.getLocalizedMessage());
        }
        return cal;

    }

    public static String TwoCharacterIntegerNumber(int num) {
        return new DecimalFormat("00").format(num);
    }

    public static void showDialog(FragmentManager fm, DialogFragment dialogFragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        dialogFragment.show(fm, dialogFragment.getClass().getSimpleName());
    }

    public static Spanned fromHtml(String source) {
        return HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    public static String getEstimationValuationAlert(Context context, String isEstimationUpdated, String valuationFlag) {
        if (isEstimationUpdated.equalsIgnoreCase(Constants.TRUE) && valuationFlag.equalsIgnoreCase(Constants.FALSE)) {
            return context.getString(R.string.estimation_update_valuation_not_completed_alert);
        } else if (isEstimationUpdated.equalsIgnoreCase(Constants.TRUE) && valuationFlag.equalsIgnoreCase(Constants.TRUE)) {
            return context.getString(R.string.estimation_verify_alert);
        } else if (isEstimationUpdated.equalsIgnoreCase(Constants.FALSE) && valuationFlag.equalsIgnoreCase(Constants.FALSE)) {
            return context.getString(R.string.complete_valuation__alert);
        } else {
            return "";
        }
    }

    public static boolean checkSelfPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static String getFormatAmount(String amount) {
        try {
            double amountDouble = Double.parseDouble(amount);
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            return nf.format(amountDouble);
        } catch (Exception e) {
            e.printStackTrace();
            return amount;
        }
    }

    public static String removeFormatAmount(String amount) {
        return amount.replace(",", "");
    }

    public static String convertTwoDigit(String amount) {
        return getFormattedDoubleString(amount, Constants.DoubleFormats.FORMAT_FOR_DIGITS, Constants.DoubleFormats.STRING_FORMAT_FOR_DIGITS);
    }

    public static String convertTwoDigit(double amount) {
        return getFormattedDoubleString(amount, Constants.DoubleFormats.FORMAT_FOR_DIGITS, Constants.DoubleFormats.STRING_FORMAT_FOR_DIGITS);
    }

    public static Uri createImageUri(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return FileProvider.getUriForFile(context, context.getPackageName(), image);
    }

    public static Uri bitmapToFile(Context context, Bitmap bitmap, File file) {
        // Initialize a new file instance to save bitmap object
        try {
            // Compress the bitmap and save in jpg format
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the saved bitmap uri
        return Uri.fromFile(file);
    }

    public static File compressImage(Context context, Uri imageUri, File destFile) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        ParcelFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openFileDescriptor(imageUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        if (fileDescriptor.getFileDescriptor() != null) {
            Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {

                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (
                        imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }


//      setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inTempStorage = new byte[16 * 1024];
            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2f, middleY - bmp.getHeight() / 2f, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly

            ExifInterface exif;
            try {
                exif = new ExifInterface(fileDescriptor.getFileDescriptor());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90: {
                        matrix.postRotate(90f);
                    }
                    case ExifInterface.ORIENTATION_ROTATE_180: {
                        matrix.postRotate(180f);
                    }
                    case ExifInterface.ORIENTATION_ROTATE_270: {
                        matrix.postRotate(270f);
                    }
                }

                if (scaledBitmap != null) {
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                            scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                            true);
                    scaledBitmap.recycle();

                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(destFile.getAbsoluteFile());

                        //  write the compressed bitmap at the destination specified by filename.
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destFile;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);

        }
        float totalPixels = width * height;
        float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


    public static File createCacheFile(Context context, Uri imageUri) {
        FileModel fileModel = getFileDetails(context, imageUri);
        return new File(context.getCacheDir(), "android_" + uniqueId() + ".png");
    }

    public static FileModel getFileDetails(Context context, Uri uri) {
        FileModel fileModel = new FileModel();
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null) {
                cursor.moveToFirst();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    String documentId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DOCUMENT_ID));
                    fileModel.setDocumentId(documentId);
                }
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));


                fileModel.setMimeType(mimeType);
                fileModel.setDisplayName(displayName);
                fileModel.setSize(size);
            }
            return fileModel;
        } catch (Exception e) {
            e.printStackTrace();
            return fileModel;
        }
    }


    public static String uniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getExtension(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
