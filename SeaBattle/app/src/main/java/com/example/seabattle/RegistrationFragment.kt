package com.example.seabattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.seabattle.databinding.FragmentRegistrationBinding

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
            // Вот этот код корректно отрабатывает, надо только придумать каклучше доставать service,
//            потому что сейчас он кажется создает новый экзепляр MainActivity
//            val username = requireActivity().findViewById<EditText>(R.id.editTextTextPersonName)
//
//            val password = requireActivity().findViewById<EditText>(R.id.editTextTextPassword)
//
//            MainActivity().getService().getApi().register(UserDto(username.text.toString(),
//                password.text.toString()))
//                .enqueue(object : Callback<BooleanResponse> {
//                    override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
//                        System.err.println(t.message)
//                    }
//
//                    override fun onResponse(
//                        call: Call<BooleanResponse>,
//                        response: Response<BooleanResponse>
//                    ) {
//                        println(response.body())
//                    }
//                });


            val fragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "loginFragment")
                .addToBackStack(null)
                .commit()

            requireActivity().supportFragmentManager.popBackStack(
                "LoginToRegistration",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}