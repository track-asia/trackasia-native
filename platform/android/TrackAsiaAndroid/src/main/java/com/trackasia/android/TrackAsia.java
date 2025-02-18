package com.trackasia.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import timber.log.Timber;

import com.trackasia.android.constants.TrackAsiaConstants;
import com.trackasia.android.exceptions.TrackAsiaConfigurationException;
import com.trackasia.android.net.ConnectivityReceiver;
import com.trackasia.android.storage.FileSource;
import com.trackasia.android.util.DefaultStyle;
import com.trackasia.android.util.TileServerOptions;
import com.trackasia.android.utils.ThreadUtils;

/**
 * The entry point to initialize the TrackAsia Android SDK.
 * <p>
 * Obtain a reference by calling {@link #getInstance(Context, String, WellKnownTileServer)}.
 * Usually this class is configured in Application#onCreate() and is responsible for the
 * active API key, application context, and connectivity state.
 * </p>
 */
@UiThread
@SuppressLint("StaticFieldLeak")
@Keep
public final class TrackAsia {

  private static final String TAG = "Mbgl-TrackAsia";
  private static ModuleProvider moduleProvider;
  private static TrackAsia INSTANCE;

  private Context context;
  @Nullable
  private String apiKey;
  @Nullable
  private TileServerOptions tileServerOptions;

  /**
   * Get an instance of TrackAsia.
   * <p>
   * This class manages the API key, application context, and connectivity state.
   * </p>
   *
   * @param context Android context which holds or is an application context
   * @return the single instance of TrackAsia
   */
  @UiThread
  @NonNull
  public static synchronized TrackAsia getInstance(@NonNull Context context) {
    ThreadUtils.init(context);
    ThreadUtils.checkThread(TAG);
    if (INSTANCE == null) {
      Context appContext = context.getApplicationContext();
      FileSource.initializeFileDirsPaths(appContext);
      INSTANCE = new TrackAsia(appContext, null);
      ConnectivityReceiver.instance(appContext);
    }

    TileServerOptions tileServerOptions = TileServerOptions.get(WellKnownTileServer.TrackAsia);
    INSTANCE.tileServerOptions = tileServerOptions;
    INSTANCE.apiKey = null;
    FileSource fileSource = FileSource.getInstance(context);
    fileSource.setTileServerOptions(tileServerOptions);
    fileSource.setApiKey(null);

    return INSTANCE;
  }

  /**
   * Get an instance of TrackAsia.
   * <p>
   * This class manages the API key, application context, and connectivity state.
   * </p>
   *
   * @param context Android context which holds or is an application context
   * @param apiKey api key
   * @param tileServer the tile server whose predefined configuration will be used to
   *                   bootstrap the SDK. The predefined configuration includes
   *                   rules for converting resource URLs between normal and canonical forms
   *                   and set of predefined styles available on the server.
   * @return the single instance of TrackAsia
   */
  @UiThread
  @NonNull
  public static synchronized TrackAsia getInstance(@NonNull Context context, @Nullable String apiKey,
                                                  WellKnownTileServer tileServer) {
    ThreadUtils.init(context);
    ThreadUtils.checkThread(TAG);
    if (INSTANCE == null) {
      Timber.plant();
      Context appContext = context.getApplicationContext();
      FileSource.initializeFileDirsPaths(appContext);
      INSTANCE = new TrackAsia(appContext, apiKey);
      ConnectivityReceiver.instance(appContext);
    } else {
      INSTANCE.apiKey = apiKey;
    }

    TileServerOptions tileServerOptions = TileServerOptions.get(tileServer);
    INSTANCE.tileServerOptions = tileServerOptions;
    FileSource fileSource = FileSource.getInstance(context);
    fileSource.setTileServerOptions(tileServerOptions);
    fileSource.setApiKey(apiKey);
    return INSTANCE;
  }

  TrackAsia(@NonNull Context context, @Nullable String apiKey) {
    this.context = context;
    this.apiKey = apiKey;
  }

  TrackAsia(@NonNull Context context, @Nullable String apiKey, @NonNull TileServerOptions options) {
    this.context = context;
    this.apiKey = apiKey;
    this.tileServerOptions = options;
  }

  /**
   * Get the current active API key for this application.
   *
   * @return API key
   */
  @Nullable
  public static String getApiKey() {
    validateTrackAsia();
    return INSTANCE.apiKey;
  }

  /**
   * Set the current active apiKey.
   */
  public static void setApiKey(String apiKey) {
    validateTrackAsia();
    throwIfApiKeyInvalid(apiKey);
    INSTANCE.apiKey = apiKey;
    FileSource.getInstance(getApplicationContext()).setApiKey(apiKey);
  }

  /**
   * Get tile server configuration.
   */
  @Nullable
  public static TileServerOptions getTileServerOptions() {
    validateTrackAsia();
    return INSTANCE.tileServerOptions;
  }

  /**
   * Get all pre-defined styles
   *
   * @return Array of predefined styles
   */
  @Nullable
  public static DefaultStyle[] getPredefinedStyles() {
    validateTrackAsia();
    if (INSTANCE.tileServerOptions != null) {
      return INSTANCE.tileServerOptions.getDefaultStyles();
    }
    return null;
  }

  /**
   * Get predefined style by name
   *
   * @return Predefined style if found
   */
  @Nullable
  public static DefaultStyle getPredefinedStyle(String name) {
    validateTrackAsia();
    if (INSTANCE.tileServerOptions != null) {
      DefaultStyle[] styles = INSTANCE.tileServerOptions.getDefaultStyles();
      for (DefaultStyle style : styles) {
        if (style.getName().equalsIgnoreCase(name)) {
          return style;
        }
      }
    }
    return null;
  }

  /**
   * Application context
   *
   * @return the application context
   */
  @NonNull
  public static Context getApplicationContext() {
    validateTrackAsia();
    return INSTANCE.context;
  }

  /**
   * Manually sets the connectivity state of the app. This is useful for apps which control their
   * own connectivity state and want to bypass any checks to the ConnectivityManager.
   *
   * @param connected flag to determine the connectivity state, true for connected, false for
   *                  disconnected, and null for ConnectivityManager to determine.
   */
  public static synchronized void setConnected(Boolean connected) {
    validateTrackAsia();
    ConnectivityReceiver.instance(INSTANCE.context).setConnected(connected);
  }

  /**
   * Determines whether we have an internet connection available. Please do not rely on this
   * method in your apps. This method is used internally by the SDK.
   *
   * @return true if there is an internet connection, false otherwise
   */
  public static synchronized Boolean isConnected() {
    validateTrackAsia();
    return ConnectivityReceiver.instance(INSTANCE.context).isConnected();
  }

  /**
   * Get the module provider
   *
   * @return moduleProvider
   */
  @NonNull
  public static ModuleProvider getModuleProvider() {
    if (moduleProvider == null) {
      moduleProvider = new ModuleProviderImpl();
    }
    return moduleProvider;
  }

  /**
   * Set the module provider. Call this as soon as possible.
   * @param  provider The ModuleProvider instance to set
   */
  public static void setModuleProvider(ModuleProvider provider) {
    moduleProvider = provider;
  }

  /**
   * Runtime validation of TrackAsia creation.
   */
  private static void validateTrackAsia() {
    if (INSTANCE == null) {
      throw new TrackAsiaConfigurationException();
    }
  }

  /**
   * Runtime validation of TrackAsia access token
   *
   * @param apiKey the access token to validate
   * @return true is valid, false otherwise
   */
  static boolean isApiKeyValid(@Nullable String apiKey) {
    if (apiKey == null) {
      return false;
    }

    apiKey = apiKey.trim().toLowerCase(TrackAsiaConstants.TRACKASIA_LOCALE);
    return !apiKey.isEmpty();
  }

  /**
   * Throws exception when access token is invalid
   */
  public static void throwIfApiKeyInvalid(@Nullable String apiKey) {
    if (!isApiKeyValid(apiKey)) {
      throw new TrackAsiaConfigurationException(
              "A valid API key is required, currently provided key is: " + apiKey);
    }
  }


  /**
   * Internal use. Check if the {@link TrackAsia#INSTANCE} is present.
   */
  public static boolean hasInstance() {
    return INSTANCE != null;
  }

  /**
   * Internal use. Returns AssetManager.
   *
   * @return the asset manager
   */
  private static AssetManager getAssetManager() {
    return getApplicationContext().getResources().getAssets();
  }
}
