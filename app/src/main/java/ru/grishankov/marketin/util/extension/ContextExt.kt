package ru.grishankov.marketin.util.extension

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import ru.grishankov.marketin.BuildConfig
import java.io.File

fun Context.openFile(file: File, remover: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(this@openFile, BuildConfig.APPLICATION_ID + ".provider", file)
        val extension = file.extension
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        mimeType?.let {
            setDataAndType(uri, it)
            remover.launch(this)
        }
    }
}