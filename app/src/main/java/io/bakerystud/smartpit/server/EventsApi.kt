package io.bakerystud.smartpit.server

import io.bakerystud.smartpit.model.EventDAO
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface EventsApi {

    @POST("pits/upload")
    fun upload(
        @Body request: List<EventDAO>
    ): Completable
}