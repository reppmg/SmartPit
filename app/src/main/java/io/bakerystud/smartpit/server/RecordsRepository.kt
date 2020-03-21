package io.bakerystud.smartpit.server

import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.storage.StoreManager
import io.reactivex.Completable
import javax.inject.Inject

class RecordsRepository @Inject constructor(
    private val storeManager: StoreManager
) {

    fun upload(data: List<RecordWithLocation>): Completable {
        return Completable.create {
            storeManager.saveLog(data)
            it.onComplete()
        }
    }
}