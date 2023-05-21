package com.example.seabattle

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seabattle.api.SeaBattleService
import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var sPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.userFormButton
        val registrationButton = binding.registration
        val errorMessage = binding.userFormErrorMessage

        sPreferences = context?.getSharedPreferences("ref", MODE_PRIVATE)

        if (isNotBlank(sPreferences?.getString(R.string.currentUsername.toString(), null))) {
            toMenuTransaction()
        }

        loginButton.setOnClickListener {

            val username = binding.userFormInclude.editTextTextPersonName.text.toString()
            val password = binding.userFormInclude.editTextTextPassword.text.toString()

            var isSuccess: Boolean

            SeaBattleService().getApi().login(
                UserDto(
                    username,
                    password
                )
            )
                .enqueue(object : Callback<BooleanResponse> {
                    override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
                        if (t::class == ConnectException::class) {
                            errorMessage.setText(R.string.lostConnection)
                        } else {
                            errorMessage.setText(R.string.unexpectedError)
                        }
                    }

                    override fun onResponse(
                        call: Call<BooleanResponse>,
                        response: Response<BooleanResponse>
                    ) {
                        isSuccess = response.isSuccessful
                        val body = response.body()!!
                        if (isSuccess && body.getMessage() == "") {
                            sPreferences!!.edit().putString(
                                R.string.currentUsername.toString(),
                                username
                            )?.apply()
                            toMenuTransaction()
                        } else {
                            errorMessage.setText(R.string.unexpectedError)
                        }
                    }
                })
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
        return string.trim().isNotBlank();
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