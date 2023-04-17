package br.com.minhasortemegasena

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.model.SupportModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    var adLoad: Int = 0
    fun uploadSupport(
        title: String,
        text: String,
        context: Context?,
        callback: ((Boolean) -> Unit)?
    ) {
        val uuid = UUID.randomUUID().toString()
        val saveSupport = SupportModel(
            id = uuid,
            title = title,
            text = text
        )
        repository.querySupportService().addOnCompleteListener {
            if (it.isSuccessful && !it.result.isEmpty) {
                if (title.isNotBlank() && text.isNotBlank()) {
                    repository.uploadSupport(saveSupport, uuid)
                    context?.let { context ->
                        Toast.makeText(
                            context,
                            "Sucesso, obrigado pelo feedback!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback?.invoke(true)
                } else {
                    context?.let {
                        Toast.makeText(
                            context,
                            "preencha todos os campos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                context?.let {
                    Toast.makeText(context, "Serviço indisponível no momento", Toast.LENGTH_LONG)
                        .show()
                }
                callback?.invoke(false)
            }

        }
    }

}