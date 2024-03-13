package com.moverbol.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.moverbol.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class MediaHelper {

    private static final String IMAGE_FILE_EXT = ".jpg";
    private static final String VIDEO_FILE_EXT = ".mp4";
    private static final String IMAGE_STORAGE_DIR = File.separator + "Images";
    private static final String VIDEO_STORAGE_DIR = File.separator + "Videos";
    private int current_code_to_send = 100;
    private HashMap<Integer, MediaCallback> mediaHolderList;
    private Media mMediaType;

    private Activity mActivity;
    private Fragment mFragment;

    public MediaHelper(Activity activity, Media type) {
        mActivity = activity;
        mediaHolderList = new HashMap<>();
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), type == Media.IMAGE ?
                    mActivity.getApplication().getString(R.string.app_name) + IMAGE_STORAGE_DIR
                    : mActivity.getApplication().getString(R.string.app_name) + VIDEO_STORAGE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
            Log.w("ssd", "creating file error: ", ex);
        }

    }

    public MediaHelper(Fragment fragment, Media type) {
        mFragment = fragment;
        mediaHolderList = new HashMap<>();

        File file = new File(type == Media.IMAGE
                ? IMAGE_STORAGE_DIR
                : VIDEO_STORAGE_DIR);

        if (!file.exists())
            file.mkdirs();
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static Bitmap getBitmapFromFile(File file, int targetSize) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), bmOptions); //it will fill bmOptions with image height & width.
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, targetSize, targetSize);

        // Decode bitmap with inSampleSize set
        bmOptions.inJustDecodeBounds = false;
        Bitmap profilePic = BitmapFactory.decodeFile(file.getPath(), bmOptions);

        try {
            ExifInterface exif = new ExifInterface(file.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Matrix m = new Matrix();
            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
            }

            profilePic = Bitmap.createBitmap(profilePic, 0, 0, profilePic.getWidth(),
                    profilePic.getHeight(), m, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profilePic;
    }

    public static int calculateInSampleSize(BitmapFactory.Options ourOption,
                                            int imageWidth, int imageHeight) {
        final int height = ourOption.outHeight;
        final int width = ourOption.outWidth;
        int inSampleSize = 1;
        if (height > imageHeight || width > imageWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) imageHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) imageWidth);
            }
        }
        return inSampleSize;
    }

    public File getNewFile(Media type) {
        try {
            File file = new File(getAlbumDir(type), type == Media.IMAGE
                    ? "img_" + System.currentTimeMillis() + IMAGE_FILE_EXT
                    : "img_" + System.currentTimeMillis() + VIDEO_FILE_EXT);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }


    public File getAlbumDir(Media type) {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(Environment.getExternalStorageDirectory(), type == Media.IMAGE
                    ? mActivity.getApplication().getString(R.string.app_name) + IMAGE_STORAGE_DIR
                    : mActivity.getApplication().getString(R.string.app_name) + VIDEO_STORAGE_DIR);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("Scouval", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v("Scouval", "External storage is not mounted " +
                    "READ/WRITE.");
        }

        return storageDir;
    }

    public void takePictureFromCamera(File file, MediaCallback callback) {
        mMediaType = Media.IMAGE;
        File currentFile = null;
        if (file != null && file.exists()) {
            currentFile = file;
        } else {
            currentFile = getNewFile(mMediaType);
        }

        Intent imageSelectIntent = new Intent();
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            imageSelectIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), currentFile));
        } else {
            imageSelectIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentFile));
        }*/
        imageSelectIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), currentFile));
        imageSelectIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        current_code_to_send = current_code_to_send + 1;

        callback.mediaType = mMediaType;
        callback.file = currentFile;
        mediaHolderList.put(current_code_to_send, callback);

        if (mActivity != null)
            mActivity.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Picture"), current_code_to_send);
        else if (mFragment != null)
            mFragment.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Picture"), current_code_to_send);
    }

    public void takePictureFromCamera(MediaCallback callback) {
        takePictureFromCamera(getNewFile(Media.IMAGE), callback);
    }

    public void takePictureFromGallery(MediaCallback callback) {
        mMediaType = Media.IMAGE;

        Intent imageSelectIntent = new Intent();
        imageSelectIntent.setAction(Intent.ACTION_PICK);
        imageSelectIntent.setType("image/*");

        current_code_to_send = current_code_to_send + 1;

        callback.mediaType = mMediaType;

        mediaHolderList.put(current_code_to_send, callback);
        if (mActivity != null)
            mActivity.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Picture"), current_code_to_send);
        else if (mFragment != null)
            mFragment.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Picture"), current_code_to_send);
    }

    public void takeVideoFromGallery(Object object, MediaCallback callback) {
        mMediaType = Media.VIDEO;
        File currentFile = null;

        Intent imageSelectIntent = new Intent();
        imageSelectIntent.setType("video/mp4");
        imageSelectIntent.setAction(Intent.ACTION_GET_CONTENT);
        current_code_to_send = current_code_to_send + 1;

        callback.mediaType = mMediaType;
        callback.file = currentFile;
        mediaHolderList.put(current_code_to_send, callback);

        if (mActivity != null)
            mActivity.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Video"), current_code_to_send);
        else if (mFragment != null)
            mFragment.startActivityForResult(Intent.createChooser(imageSelectIntent, "Select Video"), current_code_to_send);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (mediaHolderList.containsKey(requestCode)) {
                MediaCallback mediaHolder = mediaHolderList.get(requestCode);
                if (mediaHolder != null) {
                    if (mediaHolder.file == null) {
                        // means file not have so get from intent
                        try {
                            mediaHolder.file = getNewFile(mediaHolder.mediaType);

                            InputStream inputStream = null;

                            if (mActivity != null) {
                                inputStream = mActivity.getContentResolver().openInputStream(data.getData());
                            } else if (mFragment != null) {
                                inputStream = mFragment.getActivity().getContentResolver().openInputStream(data.getData());
                            }
                            if (inputStream != null) {
                                FileOutputStream fileOutputStream = new FileOutputStream(mediaHolder.file);
                                copyStream(inputStream, fileOutputStream);
                                fileOutputStream.close();
                                inputStream.close();
                            }
                        } catch (Exception e) {
                            // mediaHolder.onResult(false, null, mediaHolder.mediaType, mediaHolder.object);
                            mediaHolder.onMessageCallback(e.getMessage(), e);
                        }
                    }
                    mediaHolder.file = compressCameraImage(mediaHolder.file);
                    mediaHolder.onResult(true, mediaHolder.file, mediaHolder.mediaType);
                    mediaHolderList.remove(requestCode);
                }
            }
        }
    }

    private File compressCameraImage(File file) {
        try {
            if (file == null || file.getPath().isEmpty()) return file;

            Bitmap imgBitmap = getBitmapFromFile(file, 700);

            if (imgBitmap == null) return file;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            FileOutputStream fo;

            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public enum Media {
        IMAGE, VIDEO
    }
}