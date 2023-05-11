package com.example.seabattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seabattle.data.model.gameobjects.*
import com.example.seabattle.databinding.FragmentPreGameBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PreGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreGameFragment : Fragment() {
    private var _binding: FragmentPreGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PreGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PreGameViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val backButton = binding.preGameBackButton

                    backButton.setOnClickListener {
                        requireActivity().supportFragmentManager.popBackStack(
                            "MenuToPreGame",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    binding.preGameFormAddButton.setOnClickListener {
                        try {
                            /** Temporary usage tryAddRandom instead of tryAdd :: For Testing */
                            state.tryComplete()
                            /*shipSetBuilder?.tryAdd(
                                RectangleShip(
                                    binding.preGameFormEt1.text.toString().toInt(),
                                    binding.preGameFormEt3.text.toString().toInt(),
                                    binding.preGameFormEt2.text.toString().toInt(),
                                    binding.preGameFormEt4.text.toString().toInt())
                            )*/
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            println(e.localizedMessage)
                        } catch (e: NumberFormatException) {
                            println("Not Number")
                        }
                    }

                    binding.preGameFormDeleteButton.setOnClickListener {
                        try {
                            /** Temporary usage clear instead of tryDelete :: For Testing */
                            state.clear()
                            /*shipSetBuilder?.tryDelete(
                                RectangleShip(
                                    binding.preGameFormEt1.text.toString().toInt(),
                                    binding.preGameFormEt3.text.toString().toInt(),
                                    binding.preGameFormEt2.text.toString().toInt(),
                                    binding.preGameFormEt4.text.toString().toInt())
                            )*/
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            println(e.localizedMessage)
                        } catch (e: NumberFormatException) {
                            println("Not Number")
                        }
                    }
                    val gameButton = binding.preGameReadyButton

                    gameButton.setOnClickListener {
                        try {
                            val gameStartPack : PreGameViewModel.GameStartPack = state.tryBuild()
                            val fragment = GameFragment()
                            fragment.arguments = Bundle().apply {
                                putParcelable("gameStartPack", gameStartPack)
                            }
                            val fragmentManager = requireActivity().supportFragmentManager

                            fragmentManager.popBackStack(
                                "MenuToPreGame",
                                FragmentManager.POP_BACK_STACK_INCLUSIVE)

                            fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .hide(fragmentManager.findFragmentByTag("menuFragment")!!)
                                .add(R.id.container, fragment, "gameFragment")
                                .addToBackStack("MenuToGame")
                                .commit()
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            println(e.localizedMessage)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}