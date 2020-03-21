package io.bakerystud.smartpit.usecase

import io.bakerystud.smartpit.applyAsync
import io.bakerystud.smartpit.model.RecordWithLocation
import io.bakerystud.smartpit.server.RecordsRepository
import io.reactivex.Completable
import javax.inject.Inject

class UploadUseCase @Inject constructor(
    private val repository: RecordsRepository
) {

    fun upload(data: List<RecordWithLocation>): Completable {
        return repository.upload(data).applyAsync()
    }
}