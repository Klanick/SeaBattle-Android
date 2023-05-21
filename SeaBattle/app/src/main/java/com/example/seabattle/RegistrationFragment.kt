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
    private var progressBarVisibility : Int = View.INVISIBLE
    private var errorMessage: String = ""

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
            putInt(R.string.visible.toString(), progressBarVisibility)
            putString(R.string.errorMessage.toString(),
                errorMessage)
        }
        super.onSaveInstanceState(outState)
    }
    private fun setProgressBarVisibility(visibility: Int) {
        progressBarVisibility = visibility
        binding.progressBarRegistration.visibility = visibility
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
        setErrorMessage("")
        viewModel.liveData.observe(viewLifecycleOwner) {
            if (it == -2) {
                return@observe
            }
            if (it == -1) {
                backTransaction()
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