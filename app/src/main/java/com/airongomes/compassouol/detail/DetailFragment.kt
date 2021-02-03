package com.airongomes.compassouol.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airongomes.compassouol.R
import com.airongomes.compassouol.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


class DetailFragment : Fragment(), CustomDialog.DialogListener {

    // Referência para acessar o databinding e o viewModel fora de OnCreateView
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        // Cria o viewModel através do ViewModelFactory
        val arguments = DetailFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = DetailViewModelFactory(arguments.id)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Chama o intent locationIntent para mostrar a localização fornecida
        viewModel.locationIntent.observe(viewLifecycleOwner, Observer { locationIntent ->
            if (locationIntent != null) {
                startIntent(locationIntent)
                viewModel.clearLocationIntent()
            }
        })

        // Observar resposta do servidor
        viewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it == 200) {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.lb_registered_user),
                        2000
                    ).show()
                } else {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.lb_register_erro),
                        4000
                    ).show()
                }
                viewModel.clearServerResponse()
            }
        })

        setHasOptionsMenu(true)

        // Click Listener para escutar o botão "Participar" no evento
        binding.checkin.setOnClickListener{
            createDialog()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.compartilhar -> {
                shareCompat()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Compartilhar informações do evento com ShareCompat.IntentBuilder
     */
    private fun shareCompat() {
        val title = viewModel.event.value?.title
        val description = viewModel.event.value?.description
        val textString = "$title\n\n$description"

        val imageUri = viewModel.event.value!!.image?.toUri()?.buildUpon()?.scheme("http")?.build()

        val shareIntent = ShareCompat.IntentBuilder
            .from(requireActivity())
            .setChooserTitle(R.string.lb_share_with)
            .setType("image/png/jpg/text")
            .setStream(imageUri)
            .setText(textString)
            .intent

        startIntent(shareIntent)
    }

    /**
     * Create Intent
     * @param intent, Intent received to create
     */
    private fun startIntent(intent: Intent) {
        context?.packageManager?.let {
            if (intent.resolveActivity(it) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.message_no_perform_action),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Função responsável por chamar CustomDialog
     */
    private fun createDialog() {
        val customDialog = CustomDialog()
        customDialog.setTargetFragment(this@DetailFragment, 1)
        customDialog.show(parentFragmentManager, "MyCustomDialog")

    }

    /**
     * Override função do DialogListener para pegar as informações do usuário
     */
    override fun getResult(userName: String, userEmail: String) {
        viewModel.registerUser(userName, userEmail)
    }
}