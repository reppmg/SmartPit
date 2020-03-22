package io.bakerystud.smartpit.di.providers

import io.bakerystud.smartpit.server.EventsApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class RecordApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<EventsApi> {
    override fun get(): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }
}