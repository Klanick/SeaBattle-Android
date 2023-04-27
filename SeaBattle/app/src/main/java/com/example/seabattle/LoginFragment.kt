package com.example.seabattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seabattle.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.login
        val registrationButton = binding.registration

        loginButton.setOnClickListener {
            val fragment = MenuFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "menuFragment")
                .addToBackStack("LoginToMenu")
                .commit()
        }



        registrationButton.setOnClickListener {
            val fragment = RegistrationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "registrationFragment")
                .addToBackStack("LoginToRegistration")
                .commit()
        }
    }
}