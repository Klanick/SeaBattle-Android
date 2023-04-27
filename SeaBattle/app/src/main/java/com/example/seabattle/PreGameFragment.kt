package com.example.seabattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.seabattle.databinding.FragmentPreGameBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PreGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreGameFragment : Fragment() {
    private var _binding: FragmentPreGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameButton = binding.preGameReadyButton

        gameButton.setOnClickListener {
            val fragment = GameFragment()
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
        }
    }
}