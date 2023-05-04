package com.example.seabattle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

            MainActivity().getService().getApi().register(UserDto("a", "b"))
                .enqueue(object : Callback<BooleanResponse> {
                    override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<BooleanResponse>,
                        response: Response<BooleanResponse>
                    ) {
                        println(response.body())
                    }
                });


            val fragment = LoginFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .hide(this)
                .add(R.id.container, fragment, "loginFragment")
                .addToBackStack(null)
                .commit()
        }
    }
}