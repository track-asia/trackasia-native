package org.trackasia.android.testapp.utils

import org.trackasia.android.LibraryLoaderProvider
import org.trackasia.android.ModuleProvider
import org.trackasia.android.http.HttpRequest
import org.trackasia.android.module.loader.LibraryLoaderProviderImpl

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
