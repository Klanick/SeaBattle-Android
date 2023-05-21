package com.example.seabattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.seabattle.api.SeaBattleService
import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.api.model.UserDto.Companion.validate
import com.example.seabattle.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.userFormButton
        val errorMessage = binding.userFormErrorMessage

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
                SeaBattleService().getApi().register(user)
                    .enqueue(object : Callback<BooleanResponse> {
                        override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
                            if (t::class == ConnectException::class ||
                                    t::class == SocketTimeoutException::class
                            ) {
                                errorMessage.setText(R.string.lostConnection)
                            } else {
                                errorMessage.setText(R.string.unexpectedError)
                            }
                            binding.progressBarRegistration.visibility = View.INVISIBLE
                        }

                        override fun onResponse(
                            call: Call<BooleanResponse>,
                            response: Response<BooleanResponse>
                        ) {
                            if (response.isSuccessful && response.body()!!.getMessage() == "") {
                                backTransaction()
                            } else {
                                errorMessage.setText(R.string.unexpectedError)
                            }
                            binding.progressBarRegistration.visibility = View.INVISIBLE
                        }
                    })
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