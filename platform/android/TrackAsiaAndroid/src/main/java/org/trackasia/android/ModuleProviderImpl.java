package org.trackasia.android;

import androidx.annotation.NonNull;

import org.trackasia.android.http.HttpRequest;
import org.trackasia.android.module.http.HttpRequestImpl;
import org.trackasia.android.module.loader.LibraryLoaderProviderImpl;

public class ModuleProviderImpl implements ModuleProvider {

  @Override
  @NonNull
  public HttpRequest createHttpRequest() {
    return new HttpRequestImpl();
  }

  @NonNull
  @Override
  public LibraryLoaderProvider createLibraryLoaderProvider() {
    return new LibraryLoaderProviderImpl();
  }
}
