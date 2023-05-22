package com.example.seabattle.pre_game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seabattle.PreGameViewModel
import com.example.seabattle.data.model.gameobjects.ShipPackBuilder
import com.example.seabattle.databinding.FragmentPreGameFormForExistBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PreGameFormForNewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreGameFormForExistFragment : Fragment() {
    private var _binding: FragmentPreGameFormForExistBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PreGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment())[PreGameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreGameFormForExistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    fun closeFragment() {
                        parentFragmentManager.beginTransaction()
                            .setReorderingAllowed(true)
                            .setTransition(TRANSIT_FRAGMENT_CLOSE)
                            .hide(this@PreGameFormForExistFragment)
                            .commit()
                    }

                    fun fullClose() {
                        state.cancelChoose()
                    }

                    fun initErrorMessage() {
                        val error = state.error.value
                        if (error != null &&
                            error.position == PreGameViewModel.State.ErrorPosition.localError) {
                            binding.preGameFormErrorMessage.text = error.message
                        } else {
                            binding.preGameFormErrorMessage.text = null
                        }
                    }

                    initErrorMessage()

                    fun setErrorMessage(message : String?) {
                        if (message == null || message == "") {
                            val error = state.error.value
                            if (error != null && error.position == PreGameViewModel.State.ErrorPosition.localError) {
                                state.error.value = PreGameViewModel.State.ErrorMessage(
                                    "",
                                    PreGameViewModel.State.ErrorPosition.localError
                                )
                            }
                        } else {
                            state.error.value = PreGameViewModel.State.ErrorMessage(
                                message,
                                PreGameViewModel.State.ErrorPosition.localError
                            )
                        }

                        binding.preGameFormErrorMessage.text = message
                    }

                    state.chosenShip.observe(viewLifecycleOwner) {
                        setErrorMessage(null)
                    }

                    state.error.observe(viewLifecycleOwner) {
                        initErrorMessage()
                    }

                    binding.preGameFormCancelButton.setOnClickListener {
                        fullClose()
                    }

                    binding.preGameFormDeleteButton.setOnClickListener {
                        try {
                            state.tryDeleteChosen()
                            closeFragment()
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                           setErrorMessage(e.getMessageByContext(requireContext()))
                        }
                    }

                    state.chosenShipType.observe(viewLifecycleOwner) {
                        if (state.chosenShipType.value != PreGameViewModel.State.ChosenShipType.Exist) {
                            closeFragment()
                        }
                    }
                }
            }
        }
    }
}