package com.example.seabattle

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.api.model.UserDto.Companion.validate
import com.example.seabattle.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private var progressBarVisibility : Int = View.INVISIBLE
    private var errorMessage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(R.string.visible.toString(), progressBarVisibility)
            putString(R.string.errorMessage.toString(),
                errorMessage)
        }
        super.onSaveInstanceState(outState)
    }
    private fun setProgressBarVisibility(visibility: Int) {
        progressBarVisibility = visibility
        binding.progressBarLogin.visibility = visibility
    }

    private fun setErrorMessage(resId : Int) {
        binding.userFormErrorMessage.setText(resId)
        errorMessage = binding.userFormErrorMessage.text.toString()
    }

    private fun setErrorMessage(charSequence: CharSequence?) {
        binding.userFormErrorMessage.text = charSequence
        errorMessage = binding.userFormErrorMessage.text.toString()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            setProgressBarVisibility(getInt(R.string.visible.toString()))
            setErrorMessage(getString(R.string.errorMessage.toString()))
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.userFormButton
        val registrationButton = binding.registration

        viewModel.sPreferences = context?.getSharedPreferences("ref", MODE_PRIVATE)
        if (isNotBlank(viewModel.sPreferences?.getString(R.string.currentUsername.toString(), null))) {
            toMenuTransaction()
        }
        
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it == -2) {
                return@observe
            }
            if (it == -1) {
                toMenuTransaction()
                viewModel.liveData.postValue(-2)
                return@observe
            }
            setProgressBarVisibility(View.INVISIBLE)
            setErrorMessage(it)
            viewModel.liveData.postValue(-2)
        }

        loginButton.setOnClickListener {
            setProgressBarVisibility(View.VISIBLE)
            setErrorMessage("")

            val username = binding.userFormInclude.editTextTextPersonName.text.toString()
            val password = binding.userFormInclude.editTextTextPassword.text.toString()

            val user = UserDto(username, password)

            val validationResult = validate(user)

            if (validationResult != -1) {
                setErrorMessage(validationResult)
                setProgressBarVisibility(View.INVISIBLE)
            } else {
                viewModel.login(user)
            }
        }

        registrationButton.setOnClickListener {
            val fragment = RegistrationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .remove(this)
                .replace(R.id.container, fragment, "registrationFragment")
                .addToBackStack("LoginToRegistration")
                .commit()
        }

        binding.skipButton.setOnClickListener {
            toMenuTransaction()
        }
    }

    private fun isNotBlank(string: String?): Boolean {
        if (string == null) {
            return false
        }
        return string.trim().isNotBlank()
    }


    private fun toMenuTransaction() {
        val fragment = MenuFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, fragment, "menuFragment")
            .addToBackStack("LoginToMenu")
            .commit()
    }
}