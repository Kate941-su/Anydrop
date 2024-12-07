package com.kaitokitaya.anydrop.appError

import java.io.File

abstract class FileError: Throwable() {
    abstract val title: String
    abstract val description: String
}

// TODO: Localize
data class FailedSaveError(
    override val title: String = "Error",
    override val description: String = "Failed to save downloaded file"
) : FileError()