package io.bakerystud.smartpit.server

import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.model.toDao
import io.bakerystud.smartpit.storage.StoreManager
import io.reactivex.Completable
import javax.inject.Inject

class RecordsRepository @Inject constructor(
    private val storeManager: StoreManager,
    private val api: EventsApi
) {

    fun upload(
        data: List<RecordWithLocation>,
        events: MutableList<RecordWithLocation>
    ): Completable {
        return Completable.create {
            storeManager.saveLog(data)
            it.onComplete()
        }.mergeWith(api.upload(events.map { it.toDao() }))
    }
}