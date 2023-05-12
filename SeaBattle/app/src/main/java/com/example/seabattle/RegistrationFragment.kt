package com.example.seabattle

import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.seabattle.api.SeaBattleService
import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val loginButton = binding.registration

        loginButton.setOnClickListener {

            val username = requireActivity().findViewById<EditText>(R.id.editLoginRegistration)

            val password = requireActivity().findViewById<EditText>(R.id.editPasswordRegistration)

            var message = ""
            var isSuccess: Boolean

            SeaBattleService().getApi().register(
                UserDto(
                    username.text.toString(),
                    password.text.toString()
                )
            )
                .enqueue(object : Callback<BooleanResponse> {
                    override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
                        message = t.message.orEmpty()
                        val errorMessage =
                            requireActivity().findViewById<TextView>(R.id.errorMessageRegistration)
                        if (isEmpty(message)) {
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
                            val fragment = LoginFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .remove(this@RegistrationFragment)
                                .add(R.id.container, fragment, "loginFragment")
                                .addToBackStack(null)
                                .commit()
                        } else {
                            val errorMessage = requireActivity()
                                .findViewById<TextView>(R.id.errorMessageRegistration)
                            if (isEmpty(message)) {
                                errorMessage.text = body.getMessage()
                            } else {
                                errorMessage.text = message
                            }
                        }
                    }
                })
        }
    }
}