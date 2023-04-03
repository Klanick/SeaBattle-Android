package com.example.seabattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seabattle.databinding.FragmentGameBinding
import com.example.seabattle.databinding.FragmentMenuBinding

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameButton = binding.quitButton

        gameButton.setOnClickListener {
            val fragment = MenuFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
            fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(fragmentManager.findFragmentByTag("menuFragment")!!)
                .hide(fragmentManager.findFragmentByTag("gameFragment")!!)
                .add(R.id.container, fragment, "menuFragment")
                .addToBackStack(null)
                .commit()
        }
    }
}