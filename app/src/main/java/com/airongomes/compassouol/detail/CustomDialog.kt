package com.airongomes.compassouol.detail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.airongomes.compassouol.R

/**
 * CustomDialog responsável por requisitar informações do usuário (Nome e E-mail)
 */
class CustomDialog : DialogFragment(){

    // Instância da interface DialogListener
    private lateinit var listener: DialogListener

    /**
     * Cria o Dialog Fragment
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Cria um AlertDialog Builder
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            // Inflar o layout custom_dialog e salvar em uma variável
            val view = inflater.inflate(R.layout.custom_dialog_layout, null)

            builder.setView(view)
                // Action buttons
                .setPositiveButton(R.string.button_confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                       // Pega as Strings dos editText
                       val userName = view.findViewById<EditText>(R.id.name_text).text.toString()
                       val userEmail = view.findViewById<EditText>(R.id.email_text).text.toString()

                        if(userName.isNotBlank() && userEmail.isNotBlank()) {
                            // Adiciona as Strings obtidas na função getResult da referência
                                // da interface DialogListener
                            listener.getResult(userName, userEmail)
                            dialog.dismiss()
                        } else {
                            // Mostrar toast se o usuário não preencheu algum campo
                            Log.i("Result", "Empty or Blank values")
                            Toast.makeText(context,
                                getString(R.string.lb_empty_values),
                                Toast.LENGTH_LONG)
                                .show()
                        }


                    })
                .setNegativeButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Verifica se foi implementada a interface DialogListener no fragmento
        try {
            listener = targetFragment as DialogListener

        } catch (e: ClassCastException) {
            // Exception se acaso não foi implementada a interface no fragmento
            throw ClassCastException(
                (context.toString() +
                        " Activity/Fragment must implement DialogListener")
            )
        }

    }

    /**
     * Interface para transmitir os dados do Dialog Fragment para o DetailFragment
     */
    interface DialogListener {
        fun getResult(userName: String, userEmail: String)
    }
}