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
import com.example.seabattle.databinding.FragmentPreGameFormForNewBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PreGameFormForNewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreGameFormForNewFragment : Fragment() {
    private var _binding: FragmentPreGameFormForNewBinding? = null
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
        _binding = FragmentPreGameFormForNewBinding.inflate(inflater, container, false)
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
                            .hide(this@PreGameFormForNewFragment)
                            .commit()
                    }

                    fun fullClose() {
                        state.cancelChoose()
                    }

                    fun setErrorMessage(message : String?) {
                        if (message == null || message == "") {
                            state.errorPosition.value = null
                        } else {
                            state.errorPosition.value = PreGameViewModel.State.ErrorPosition.localError
                        }

                        binding.preGameFormErrorMessage.text = message
                    }

                    state.chosenShip.observe(viewLifecycleOwner) {
                        setErrorMessage(null)
                    }

                    state.errorPosition.observe(viewLifecycleOwner) {
                        val pos = state.errorPosition.value
                        if (pos != null && pos != PreGameViewModel.State.ErrorPosition.localError) {
                            setErrorMessage(null)
                        }
                    }

                    binding.preGameFormCancelButton.setOnClickListener {
                        fullClose()
                    }

                    binding.preGameFormAddButton.setOnClickListener {
                        try {
                            state.tryAddChosen()
                            closeFragment()
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            setErrorMessage(e.getMessageByContext(requireContext()))
                        }
                    }

                    state.chosenShipType.observe(viewLifecycleOwner) {
                        if (state.chosenShipType.value != PreGameViewModel.State.ChosenShipType.New) {
                            closeFragment()
                        }
                    }
                }
            }
        }
    }
}