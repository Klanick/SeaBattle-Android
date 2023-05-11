package com.example.seabattle

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.seabattle.api.SeaBattleService
import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            val username = requireActivity().findViewById<EditText>(R.id.editLoginLogin)

            val password = requireActivity().findViewById<EditText>(R.id.editPasswordLogin)

            var message = ""
            var isSuccess: Boolean

            System.err.println(username.text.toString() + ":" + password.text.toString())

            SeaBattleService().getApi().login(
                UserDto(
                    username.text.toString(),
                    password.text.toString()
                )
            )
                .enqueue(object : Callback<BooleanResponse> {
                    override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
                        message = t.message.orEmpty()
                        val errorMessage = requireActivity()
                            .findViewById<TextView>(R.id.errorMessageLogin)
                        if (TextUtils.isEmpty(message)) {
                            errorMessage.setText(R.string.unexpectedError)
                        } else {
                            errorMessage.text = message
                        }
                    }

                    override fun onResponse(
                        call: Call<BooleanResponse>,
                        response: Response<BooleanResponse>
                    ) {
                        isSuccess = response.isSuccessful
                        val body = response.body()!!
                        if (isSuccess && body.getMessage() == "") {
                            val fragment = MenuFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .remove(this@LoginFragment)
                                .add(R.id.container, fragment, "menuFragment")
                                .addToBackStack(null)
                                .commit()
                        } else {
                            val errorMessage =
                                requireActivity().findViewById<TextView>(R.id.errorMessageLogin)
                            if (TextUtils.isEmpty(message)) {
                                errorMessage.text = body.getMessage()
                            } else {
                                errorMessage.text = message
                            }
                        }
                    }
                })
        }

        registrationButton.setOnClickListener {
            val fragment = RegistrationFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .remove(this)
                .add(R.id.container, fragment, "registrationFragment")
                .addToBackStack(null)
                .commit()
        }
    }
}