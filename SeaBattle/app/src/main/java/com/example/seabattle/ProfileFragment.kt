package com.example.seabattle

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.seabattle.api.SeaBattleService
import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.StatisticDto
import com.example.seabattle.api.model.UserDto
import com.example.seabattle.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var sPreferences: SharedPreferences? = null
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exitProfileButton = binding.profileExit
        val errorMessage = binding.errorMessage
        sPreferences  = context?.getSharedPreferences("ref", Context.MODE_PRIVATE)

        val realUsername = sPreferences?.getString(R.string.currentUsername.toString(), "").orEmpty()

        setTextView(R.id.Nickname, realUsername)


        SeaBattleService().getApi().getStatistic(realUsername)
            .enqueue(object : Callback<StatisticDto> {
                override fun onFailure(call: Call<StatisticDto>, t: Throwable) {
                    errorMessage.setText(R.string.unexpectedError)
                }

                override fun onResponse(
                    call: Call<StatisticDto>,
                    response: Response<StatisticDto>
                ) {
                    viewModel.statistic = response.body()
                    setTextView(R.id.shipsLostCount, viewModel.statistic?.getTotalShipsLost().toString())
                    setTextView(R.id.shipsDestroyedCount, viewModel.statistic?.getTotalShipsDestroyed().toString())
                    setTextView(R.id.victoriesCount, viewModel.statistic?.getTotalWins().toString())
                    setTextView(R.id.defeatsCount, viewModel.statistic?.getTotalLoses().toString())
                }
            })
        exitProfileButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(
                "MenuToProfile",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun setTextView(id: Int, text: String) {
        requireActivity().findViewById<TextView>(id).text = text
    }
}