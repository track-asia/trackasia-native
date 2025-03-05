package com.trackasia.android.testapp.utils

import com.trackasia.android.LibraryLoaderProvider
import com.trackasia.android.ModuleProvider
import com.trackasia.android.http.HttpRequest
import com.trackasia.android.module.loader.LibraryLoaderProviderImpl
import com.trackasia.android.testapp.utils.ExampleHttpRequestImpl

/*
 * An example implementation of the ModuleProvider. This is useful primarily for providing
 * a custom implementation of HttpRequest used by the core.
 */
class ExampleCustomModuleProviderImpl : ModuleProvider {
    override fun createHttpRequest(): HttpRequest {
        return ExampleHttpRequestImpl()
    }

    override fun createLibraryLoaderProvider(): LibraryLoaderProvider {
        return LibraryLoaderProviderImpl()
    }
}
