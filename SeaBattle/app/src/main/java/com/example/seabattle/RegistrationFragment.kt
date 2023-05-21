package com.example.seabattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.api.model.UserDto.Companion.validate
import com.example.seabattle.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(R.string.visible.toString(), binding.progressBarRegistration.visibility)
            putString(R.string.errorMessage.toString(),
                binding.userFormErrorMessage.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            binding.progressBarRegistration.visibility = getInt(R.string.visible.toString())
            binding.userFormErrorMessage.text = getString(R.string.errorMessage.toString())
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.userFormButton
        val errorMessage = binding.userFormErrorMessage
        errorMessage.text = ""
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it == -2) {
                return@observe
            }
            if (it == -1) {
                backTransaction()
                viewModel.liveData.postValue(-2)
                return@observe
            }
            binding.progressBarRegistration.visibility = View.INVISIBLE
            errorMessage.setText(it)
            viewModel.liveData.postValue(-2)
        }

        loginButton.setOnClickListener {
            binding.progressBarRegistration.visibility = View.VISIBLE
            errorMessage.text = ""

            val username = binding.userFormInclude.editTextTextPersonName.text.toString()
            val password = binding.userFormInclude.editTextTextPassword.text.toString()

            val user = UserDto(username, password)

            val validationResult = validate(user)

            if (validationResult != -1) {
                errorMessage.setText(validationResult)
                binding.progressBarRegistration.visibility = View.INVISIBLE
            } else {
                viewModel.register(user)
            }
        }

        binding.backButton.setOnClickListener {
            backTransaction()
        }
    }

    private fun backTransaction() {
        requireActivity().supportFragmentManager.popBackStack(
            "LoginToRegistration",
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}