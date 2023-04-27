package com.example.seabattle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seabattle.databinding.FragmentMenuBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameButton = binding.gameButton

        gameButton.setOnClickListener {
            val fragment = PreGameFragment()
            //requireActivity() - is bad decision, but we will have only one const activity, so...
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "preGameFragment")
                .addToBackStack(null)
                .commit()
        }
        val exitButton = binding.exitButton

        exitButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
        val profileButton = binding.profileButton

        profileButton.setOnClickListener {
            val fragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "profileFragment")
                .addToBackStack(null)
                //or just .replace(R.id.container, fragment)
                .commit()
        }
    }
}