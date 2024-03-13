package com.moverbol.util;

import java.io.File;

/**
 * Creaded by Dipen jansari
 */
public abstract class MediaCallback {

    public File file;
    MediaHelper.Media mediaType;

    public abstract void onResult(boolean status, File file, MediaHelper.Media mediaType);

    void onMessageCallback(String message, Exception exception) {

    }
}