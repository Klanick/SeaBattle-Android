package com.example.seabattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.seabattle.data.model.gameobjects.Ship
import com.example.seabattle.databinding.FragmentGameBinding

private const val ARG_SHIPS = "ships"
/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var ships: List<Ship>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //TODO replace from deprecated
            ships = it.getParcelableArrayList(ARG_SHIPS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quitButton = binding.quitButton

        quitButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(
                "MenuToGame",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}