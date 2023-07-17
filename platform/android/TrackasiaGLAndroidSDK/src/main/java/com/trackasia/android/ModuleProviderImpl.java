package com.trackasia.android;

import androidx.annotation.NonNull;

import com.trackasia.android.http.HttpRequest;
import com.trackasia.android.module.http.HttpRequestImpl;
import com.trackasia.android.module.loader.LibraryLoaderProviderImpl;

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
