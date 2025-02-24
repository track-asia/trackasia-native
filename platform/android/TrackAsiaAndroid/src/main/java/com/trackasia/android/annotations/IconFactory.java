package com.trackasia.android.annotations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.trackasia.android.MapStrictMode;
import com.trackasia.android.R;
import com.trackasia.android.exceptions.TooManyIconsException;
import com.trackasia.android.utils.BitmapUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Factory for creating Icons from bitmap images.
 * <p>
 * icon is used to display bitmaps on top of the map using {@link Marker}.
 * </p>
 *
 * @deprecated As of 7.0.0,
 * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
 * TrackAsia Annotation Plugin</a> instead
 */
@Deprecated
public final class IconFactory {

  private static final String ICON_ID_PREFIX = "com.mapbox.icons.icon_";

  private Context context;
  @SuppressLint("StaticFieldLeak")
  private static IconFactory instance;
  private Icon defaultMarker;
  private BitmapFactory.Options options;

  private int nextId = 0;

  /**
   * Get a single instance of IconFactory.
   *
   * @param context the context to derive the application context from
   * @return the single instance of IconFactory
   */
  public static synchronized IconFactory getInstance(@NonNull Context context) {
    if (instance == null) {
      instance = new IconFactory(context.getApplicationContext());
    }
    return instance;
  }

  private IconFactory(@NonNull Context context) {
    this.context = context;
    DisplayMetrics realMetrics = null;
    DisplayMetrics metrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      realMetrics = new DisplayMetrics();
      wm.getDefaultDisplay().getRealMetrics(realMetrics);
    }
    wm.getDefaultDisplay().getMetrics(metrics);

    options = new BitmapFactory.Options();
    options.inScaled = true;
    options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
    options.inTargetDensity = metrics.densityDpi;
    if (realMetrics != null) {
      options.inScreenDensity = realMetrics.densityDpi;
    }
  }

  /**
   * Creates an icon from a given Bitmap image.
   *
   * @param bitmap image used for creating the Icon.
   * @return The icon using the given Bitmap image.
   */
  public Icon fromBitmap(@NonNull Bitmap bitmap) {
    if (nextId < 0) {
      throw new TooManyIconsException();
    }
    String id = ICON_ID_PREFIX + ++nextId;
    return new Icon(id, bitmap);
  }

  /**
   * Creates an icon using the resource ID of a Bitmap image.
   *
   * @param resourceId The resource ID of a Bitmap image.
   * @return The icon that was loaded from the asset or {@code null} if failed to load.
   */
  public Icon fromResource(@DrawableRes int resourceId) {
    Drawable drawable = BitmapUtils.getDrawableFromRes(context, resourceId);
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
      return fromBitmap(bitmapDrawable.getBitmap());
    } else {
      throw new IllegalArgumentException("Failed to decode image. The resource provided must be a Bitmap.");
    }
  }

  /**
   * Provides an icon using the default marker icon used for {@link Marker}.
   *
   * @return An icon with the default {@link Marker} icon.
   */
  public Icon defaultMarker() {
    if (defaultMarker == null) {
      defaultMarker = fromResource(R.drawable.trackasia_marker_icon_default);
    }
    return defaultMarker;
  }

  private Icon fromInputStream(@NonNull InputStream is) {
    Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
    return fromBitmap(bitmap);
  }

  /**
   * Creates an Icon using the name of a Bitmap image in the assets directory.
   *
   * @param assetName The name of a Bitmap image in the assets directory.
   * @return The Icon that was loaded from the asset or null if failed to load.
   */
  public Icon fromAsset(@NonNull String assetName) {
    InputStream is;
    try {
      is = context.getAssets().open(assetName);
    } catch (IOException ioException) {
      MapStrictMode.strictModeViolation(ioException);
      return null;
    }
    return fromInputStream(is);
  }

  /**
   * Creates an Icon using the absolute file path of a Bitmap image.
   *
   * @param absolutePath The absolute path of the Bitmap image.
   * @return The Icon that was loaded from the absolute path or null if failed to load.
   */
  public Icon fromPath(@NonNull String absolutePath) {
    Bitmap bitmap = BitmapFactory.decodeFile(absolutePath, options);
    return fromBitmap(bitmap);
  }

  /**
   * Create an Icon using the name of a Bitmap image file located in the internal storage.
   * In particular, this calls Context#openFileInput(String).
   *
   * @param fileName The name of the Bitmap image file.
   * @return The Icon that was loaded from the asset or null if failed to load.
   * @see <a href="https://developer.android.com/guide/topics/data/data-storage.html#filesInternal">
   * Using the Internal Storage</a>
   */
  public Icon fromFile(@NonNull String fileName) {
    FileInputStream is;
    try {
      is = context.openFileInput(fileName);
    } catch (FileNotFoundException fileNotFoundException) {
      MapStrictMode.strictModeViolation(fileNotFoundException);
      return null;
    }
    return fromInputStream(is);
  }

  /**
   * Create an Icon using a previously created icon identifier along with a provided Bitmap.
   *
   * @param iconId The Icon identifier you'd like to recreate.
   * @param bitmap a Bitmap used to replace the current one.
   * @return The Icon using the new Bitmap.
   */
  public static Icon recreate(@NonNull String iconId, @NonNull Bitmap bitmap) {
    return new Icon(iconId, bitmap);
  }

}
