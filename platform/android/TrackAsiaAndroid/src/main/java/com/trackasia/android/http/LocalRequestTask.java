package com.trackasia.android.http;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trackasia.android.MapStrictMode;
import com.trackasia.android.TrackAsia;
import com.trackasia.android.log.Logger;

import java.io.IOException;
import java.io.InputStream;

class LocalRequestTask extends AsyncTask<String, Void, byte[]> {

  private static final String TAG = "Mbgl-LocalRequestTask";

  private OnLocalRequestResponse requestResponse;

  LocalRequestTask(OnLocalRequestResponse requestResponse) {
    this.requestResponse = requestResponse;
  }

  @Nullable
  @Override
  protected byte[] doInBackground(String... strings) {
    return loadFile(TrackAsia.getApplicationContext().getAssets(),
      "integration/" + strings[0]
        .substring(8)
        .replaceAll("%20", " ")
        .replaceAll("%2c", ","));
  }

  @Override
  protected void onPostExecute(@Nullable byte[] bytes) {
    super.onPostExecute(bytes);
    if (bytes != null && requestResponse != null) {
      requestResponse.onResponse(bytes);
    }
  }

  @Nullable
  private static byte[] loadFile(AssetManager assets, @NonNull String path) {
    byte[] buffer = null;
    InputStream input = null;
    try {
      input = assets.open(path);
      int size = input.available();
      buffer = new byte[size];
      input.read(buffer);
    } catch (IOException exception) {
      logFileError(exception);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException exception) {
          logFileError(exception);
        }
      }
    }
    return buffer;
  }

  private static void logFileError(Exception exception) {
    String message = "Load file failed";
    Logger.e(TAG, message, exception);
    MapStrictMode.strictModeViolation(message, exception);
  }

  public interface OnLocalRequestResponse {
    void onResponse(byte[] bytes);
  }
}
