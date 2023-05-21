package com.example.seabattle

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.seabattle.databinding.FragmentMenuBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private var sPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sPreferences = context?.getSharedPreferences("ref", Context.MODE_PRIVATE)

        if (!isNotBlank(sPreferences?.getString(R.string.currentUsername.toString(), null))) {
            binding.profileButton.isEnabled = false
        }
        val gameButton = binding.gameButton

        gameButton.setOnClickListener {
            val fragment = PreGameFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "preGameFragment")
                .addToBackStack("MenuToPreGame")
                .commit()
        }
        val exitButton = binding.exitButton

        sPreferences = context?.getSharedPreferences("ref", Context.MODE_PRIVATE)

        exitButton.setOnClickListener {
            sPreferences?.edit()?.remove(R.string.currentUsername.toString())?.apply()
            requireActivity().supportFragmentManager.popBackStack(
                "LoginToMenu",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        val profileButton = binding.profileButton

        profileButton.setOnClickListener {
            val fragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "profileFragment")
                .addToBackStack("MenuToProfile")
                .commit()
        }
    }

    private fun isNotBlank(string: String?): Boolean {
        if (string == null) {
            return false
        }
        return string.trim().isNotBlank()
    }
}