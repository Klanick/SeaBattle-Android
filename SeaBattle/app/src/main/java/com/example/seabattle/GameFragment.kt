package com.example.seabattle

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seabattle.adapter.CellAdapter
import com.example.seabattle.data.model.gameobjects.Cell
import com.example.seabattle.data.model.gameobjects.Ship
import com.example.seabattle.databinding.FragmentGameBinding

private const val ARG_SHIPS = "ships"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var ships: List<Ship>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        arguments?.let {
            // TODO replace from deprecated
            ships = it.getParcelableArrayList(ARG_SHIPS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.ourShip1.adapter = CellAdapter(getCells())
        binding.ourShip2.adapter = CellAdapter(getCells())
        binding.ourShip3.adapter = CellAdapter(getCells())
        binding.ourShip4.adapter = CellAdapter(getCells())
        binding.ourShip5.adapter = CellAdapter(getCells())
        binding.ourShip6.adapter = CellAdapter(getCells())
        binding.ourShip7.adapter = CellAdapter(getCells())
        binding.ourShip8.adapter = CellAdapter(getCells())
        binding.ourShip9.adapter = CellAdapter(getCells())
        binding.ourShip10.adapter = CellAdapter(getCells())

        binding.ourShip1.layoutManager = LinearLayoutManager(activity)
        binding.ourShip2.layoutManager = LinearLayoutManager(activity)
        binding.ourShip3.layoutManager = LinearLayoutManager(activity)
        binding.ourShip4.layoutManager = LinearLayoutManager(activity)
        binding.ourShip5.layoutManager = LinearLayoutManager(activity)
        binding.ourShip6.layoutManager = LinearLayoutManager(activity)
        binding.ourShip7.layoutManager = LinearLayoutManager(activity)
        binding.ourShip8.layoutManager = LinearLayoutManager(activity)
        binding.ourShip9.layoutManager = LinearLayoutManager(activity)
        binding.ourShip10.layoutManager = LinearLayoutManager(activity)

        binding.recyclerview1.adapter = CellAdapter(getCells())
        binding.recyclerview1.layoutManager = LinearLayoutManager(activity)

        binding.recyclerview2.adapter = CellAdapter(getCells())
        binding.recyclerview2.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview3.adapter = CellAdapter(getCells())
        binding.recyclerview3.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview4.adapter = CellAdapter(getCells())
        binding.recyclerview4.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview5.adapter = CellAdapter(getCells())
        binding.recyclerview5.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview6.adapter = CellAdapter(getCells())
        binding.recyclerview6.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview7.adapter = CellAdapter(getCells())
        binding.recyclerview7.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview8.adapter = CellAdapter(getCells())
        binding.recyclerview8.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview9.adapter = CellAdapter(getCells())
        binding.recyclerview9.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview10.adapter = CellAdapter(getCells())
        binding.recyclerview10.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quitButton = binding.quitButton

        quitButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(
                "MenuToGame",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    override fun onDestroy() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }

    private fun getCells(): ArrayList<Cell> {
        return arrayListOf(Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0),Cell(0,0))
    }
}