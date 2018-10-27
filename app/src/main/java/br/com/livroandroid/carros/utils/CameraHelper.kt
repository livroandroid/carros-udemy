package br.com.livroandroid.carros.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class CameraHelper {
    companion object {
        private const val TAG = "carros"
    }

    // Arquivo pode ser nulo
    var file: File? = null
        private set

    // Se girou a tela recupera o estado
    fun init(icicle: Bundle?,onInit: (file: File) -> Unit) {
        if (icicle != null) {
            val f = icicle.getSerializable("file") as File?
            if(f != null) {
                onInit(f)
                this.file = f
            }
        }
    }

    // Salva o estado
    fun onSaveInstanceState(outState: Bundle) {
        if (file != null) {
            outState.putSerializable("file", file)
        }
    }

    // Intent para abrir a câmera
    fun open(context: Context, fileName: String): Intent {
        file = getFile(context, fileName)
        Log.d(TAG,"Foto: ${file?.absolutePath}")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file?.apply {
            val authority = context.applicationContext.packageName + ".provider"
            val uri = FileProvider.getUriForFile(context, authority, this)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        return cameraIntent
    }

    // Cria o arquivo da foto no diretório privado do app
    private fun getFile(context: Context, fileName: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: throw RuntimeException("Não foi possível criar a o arquivo da foto.")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    // Lê o bitmap no tamanho desejado
    fun getBitmap(w: Int, h: Int): Bitmap? {
        file?.apply {
            if (exists()) {
                Log.d(TAG, absolutePath)
                // Resize
                val bitmap = ImageUtils.resize(this, w, h)

                // Salva o arquivo com resize
                save(bitmap)

                return bitmap
            }
        }
        return null
    }

    // Salva o bitmap reduzido no arquivo (para upload)
    private fun save(bitmap: Bitmap) {
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.d(TAG, "Foto salva: $absolutePath")
        }
    }
}
