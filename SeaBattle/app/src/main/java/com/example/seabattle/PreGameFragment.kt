package com.example.seabattle

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seabattle.data.model.gameobjects.*
import com.example.seabattle.databinding.FragmentPreGameBinding
import com.example.seabattle.pre_game.PreGameFormForExistFragment
import com.example.seabattle.pre_game.PreGameFormForNewFragment
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [PreGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreGameFragment : Fragment() {
    private var _binding: FragmentPreGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PreGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PreGameViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val gridLayout = binding.preGameGameBoard.preGameGrid
                    gridLayout.rowCount = 10
                    gridLayout.columnCount = 10
                    val cellViews = ArrayList<FrameLayout>()
                    for (x in 0 until 10) {
                        for (y in 0 until 10) {
                            val cellView = layoutInflater.inflate(R.layout.cell, gridLayout, false)
                                    as FrameLayout
                            cellView.visibility = View.VISIBLE
                            cellView.isClickable = true
                            cellView.id
                            cellView.setOnClickListener {
                                state.clickCell(Cell(x, y))
                            }
                            cellViews.add(cellView)
                        }
                    }


                    fun cellToView(cell : Cell, views : List<FrameLayout>) : FrameLayout {
                        return views[cell.posX*10 + cell.posY]
                    }
                    fun colorCell(cellView : FrameLayout, colorId : Int) {
                        val cardView = (cellView[0] as CardView)
                        cardView.setCardBackgroundColor(requireContext().resources.getColor(colorId, null))
                    }
                    fun reColorAll(views : List<FrameLayout>) {
                        views.forEach{ cellView ->
                            colorCell(cellView, R.color.blue_100)
                        }

                        state.shipCells().forEach { cell: Cell ->
                            colorCell(cellToView(cell, views), R.color.blue_500)
                        }

                        state.getExistShipCells().forEach { cell: Cell ->
                            colorCell(cellToView(cell, views), R.color.red_a200)
                        }

                        state.getNewShipCells().forEach { cell: Cell ->
                            colorCell(cellToView(cell, views), R.color.add)
                        }
                    }

                    reColorAll(cellViews)

                    fun setErrorMessage(message : String?) {
                        if (message == null || message == "") {
                            state.errorPosition.value = null
                        } else {
                            state.errorPosition.value = PreGameViewModel.State.ErrorPosition.globalError
                        }

                        binding.preGameReadyErrorMessage.text = message
                    }

                    state.packWasChanged.observe(viewLifecycleOwner) {
                        reColorAll(cellViews)
                        setErrorMessage(null)
                    }
                    state.chosenShip.observe(viewLifecycleOwner) {
                        reColorAll(cellViews)
                    }
                    state.chosenShipType.observe(viewLifecycleOwner) {
                        val type = state.chosenShipType.value
                        if (type != null) {
                            val formFragment = if (type == PreGameViewModel.State.ChosenShipType.New) {
                                PreGameFormForNewFragment()
                            } else {
                                PreGameFormForExistFragment()
                            }
                            val formContainer = R.id.pre_game__form_container

                            childFragmentManager.fragments.forEach { fragment ->
                                childFragmentManager.beginTransaction()
                                    .remove(fragment)
                                    .commit() }

                            childFragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .add(formContainer, formFragment, "preGameFormFragment")
                                .setTransition(TRANSIT_FRAGMENT_OPEN)
                                .commit()
                        }
                    }
                    cellViews.forEach(gridLayout::addView)

                    val backButton = binding.preGameBackButton

                    backButton.setOnClickListener {
                        requireActivity().supportFragmentManager.popBackStack(
                            "MenuToPreGame",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }



                    state.errorPosition.observe(viewLifecycleOwner) {
                        val pos = state.errorPosition.value
                        if (pos != null && pos != PreGameViewModel.State.ErrorPosition.globalError) {
                            setErrorMessage(null)
                        }
                    }

                    binding.preGameAutoButton.setOnClickListener {
                        try {
                            state.tryComplete()
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            setErrorMessage(e.getMessageByContext(requireContext()))
                        }
                    }

                    binding.preGameClearButton.setOnClickListener {
                        state.clear()
                    }
                    val gameButton = binding.preGameReadyButton

                    gameButton.setOnClickListener {
                        try {
                            val gameStartPack : PreGameViewModel.GameStartPack = state.tryBuild()
                            val fragment = GameFragment()
                            fragment.arguments = Bundle().apply {
                                putParcelable("gameStartPack", gameStartPack)
                            }
                            val fragmentManager = requireActivity().supportFragmentManager

                            fragmentManager.popBackStack(
                                "MenuToPreGame",
                                FragmentManager.POP_BACK_STACK_INCLUSIVE)

                            fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .hide(fragmentManager.findFragmentByTag("menuFragment")!!)
                                .add(R.id.container, fragment, "gameFragment")
                                .addToBackStack("MenuToGame")
                                .commit()
                        } catch (e: ShipPackBuilder.ShipSetBuilderException) {
                            setErrorMessage(e.getMessageByContext(requireContext()))
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}