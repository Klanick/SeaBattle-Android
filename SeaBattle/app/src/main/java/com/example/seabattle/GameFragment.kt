package com.example.seabattle

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seabattle.bot.SeaBattleBot
import com.example.seabattle.data.model.gameobjects.Cell
import com.example.seabattle.data.model.gameobjects.Ship
import com.example.seabattle.databinding.FragmentGameBinding
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

private const val ARG_GAME_START_PACK = "gameStartPack"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var gameStartPack: PreGameViewModel.GameStartPack
    private lateinit var viewModel: PreGameViewModel
    private var turn: Turn = Turn.USER
    private val bot = SeaBattleBot()
    private lateinit var yourTurn: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
        yourTurn = resources.getText(R.string.YourTurn)
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this)[PreGameViewModel::class.java]
        arguments?.let {
            @Suppress("DEPRECATION")
            gameStartPack = it.getParcelable(ARG_GAME_START_PACK)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exitButton = binding.exitButton
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    val textLayout = binding.gameTextLayout
                    val ourLayout = binding.ourGameGrid
                    val botLayout = binding.botGameGrid
                    botLayout.rowCount = 10
                    botLayout.columnCount = 10
                    ourLayout.rowCount = 10
                    ourLayout.columnCount = 10
                    textLayout.rowCount = 1
                    textLayout.columnCount = 1
                    val gameTextView = layoutInflater.inflate(
                        R.layout.game_text,
                        binding.gameTextLayout,
                        false
                    ) as TextView
                    gameTextView.visibility = View.VISIBLE
                    gameTextView.isClickable = false
                    gameTextView.text = resources.getText(R.string.YourTurn)
                    textLayout.addView(gameTextView)

                    val imageViews = ArrayList<FrameLayout>()
                    val imageViews2 = ArrayList<FrameLayout>()

                    for (y in 0 until 10) {
                        for (x in 0 until 10) {
                            val cellView2 =
                                layoutInflater.inflate(R.layout.cell, binding.ourGameGrid, false)
                                        as FrameLayout
                            cellView2.visibility = View.VISIBLE
                            cellView2.isClickable = false

                            if (gameStartPack.ships.any { ship -> ship.include(Cell(x, y)) }) {
                                (cellView2[0] as CardView).setCardBackgroundColor(
                                    resources.getColor(
                                        R.color.blue_500
                                    )
                                )
                            }
                            imageViews2.add(cellView2)
                        }
                    }

                    for (y in 0 until 10) {
                        for (x in 0 until 10) {
                            val cellView =
                                layoutInflater.inflate(R.layout.cell, binding.botGameGrid, false)
                                        as FrameLayout
                            cellView.visibility = View.VISIBLE
                            cellView.isClickable = true

                            cellView.setOnClickListener {
                                if (turn != Turn.USER || ((cellView[0] as CardView)[0] as TextView).text != "") {
                                    return@setOnClickListener
                                }
                                // ------ Запрешаем ходить ------
                                turn = Turn.BOT


                                println("Click on cell:($x;$y)")
                                if (gameStartPack.opponentShips.any { ship ->
                                        ship.include(Cell(x, y))
                                    }) {
                                    val ship = gameStartPack.opponentShips.first { ship ->
                                        ship.include(Cell(x, y))
                                    }

                                    ((cellView[0] as CardView)[0] as TextView).text = "X"
                                    ((cellView[0] as CardView)[0] as TextView).setTextColor(
                                        resources.getColor(R.color.red_a200)
                                    )

                                    var toast = Toast.makeText(
                                        context,
                                        resources.getText(R.string.FoundCell),
                                        Toast.LENGTH_SHORT
                                    )

                                    /// --------------------- SHIP KILLED ---------------------
                                    if (ship.getCells()
                                            .all { cell -> ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text == "X" }
                                    ) {
                                        ship.getCells().forEach { cell ->
                                            ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).setTextColor(
                                                resources.getColor(R.color.black)
                                            )
                                            (imageViews[cell.posY * 10 + cell.posX][0] as CardView).setCardBackgroundColor(
                                                resources.getColor(R.color.blue_500)
                                            )
                                        }
                                        (cellView[0] as CardView).setCardBackgroundColor(
                                            resources.getColor(
                                                R.color.blue_500
                                            )
                                        )

                                        fillAllAround(ship, imageViews)

                                        toast = Toast.makeText(
                                            context,
                                            resources.getText(R.string.FoundShip),
                                            Toast.LENGTH_SHORT
                                        )

                                        if (win(imageViews, gameStartPack.opponentShips)) {
                                            gameTextView.text = resources.getText(R.string.YouWin)

                                            val handler = Handler()
                                            handler.postDelayed(
                                                {
                                                    requireActivity().supportFragmentManager.popBackStack(
                                                        "MenuToGame",
                                                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                                                    )
                                                }, 3000
                                            )
                                            turn = Turn.NONE
                                        }
                                    }

                                    toast.show()
                                    val handler = Handler()
                                    handler.postDelayed({ toast.cancel() }, 1300)
                                    // ----------- Позволяем ходить -----------
                                    turn = Turn.USER
                                } else {
                                    ((cellView[0] as CardView)[0] as TextView).text = "O"

                                    gameTextView.text = resources.getText(R.string.BotTurn)

                                    val toast = Toast.makeText(
                                        context,
                                        resources.getText(R.string.Miss),
                                        Toast.LENGTH_SHORT
                                    )

                                    toast.show()
                                    val handler = Handler()
                                    handler.postDelayed({ toast.cancel() }, 500)

                                    /// --------------ALARM--------------_BOT_ATTACKS_--------------ALARM--------------

                                    botAttacks(imageViews2, gameTextView)
                                }
                            }
                            imageViews.add(cellView)
                        }
                    }

                    imageViews.forEach(ourLayout::addView)
                    imageViews2.forEach(botLayout::addView)

                    exitButton.setOnClickListener {
                        requireActivity().supportFragmentManager.popBackStack(
                            "MenuToGame",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                }
            }
        }

    }

    private fun win(imageViews: ArrayList<FrameLayout>, ships: List<Ship>): Boolean {
        return ships.all {
            it.getCells().all { cell ->
                ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text == "X"
            }
        }
    }

    private fun botAttacks(imageViews: ArrayList<FrameLayout>, gameTextView: TextView) {
        thread {
            if (turn != Turn.BOT) {
                return@thread
            }
            Thread.sleep(1500)
            var cell = bot.getCell()
            while (((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text != "") {
                cell = bot.getCell()
            }

            if (getOurShips().any { it.include(cell) }) {
                bot.foundCell(cell)
                ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text = "X"

                val ship = getOurShips().first { it.include(cell) }
                // --------------------- SHIP KILLED ---------------------
                if (ship.getCells()
                        .all { ((imageViews[it.posY * 10 + it.posX][0] as CardView)[0] as TextView).text == "X" }
                ) {
                    bot.foundShip()
                    fillAllAround(ship, imageViews)
                }

                if (win(imageViews, gameStartPack.ships)) {
                    gameTextView.text = resources.getText(R.string.YouLose)
                    lose()
                }

                // --------------------- NEXT ATTACK ---------------------
                botAttacks(imageViews, gameTextView)
            } else {
                ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text = "O"
                gameTextView.text = yourTurn
                turn = Turn.USER
            }
        }
    }

    private fun lose() {
        turn = Turn.NONE
        thread {
            Thread.sleep(3000)
            requireActivity().supportFragmentManager.popBackStack(
                "MenuToGame",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun fillAllAround(ship: Ship, imageViews: ArrayList<FrameLayout>) {
        ship.getContainerCells().forEach { cell ->
            if (cell.posX > -1 && cell.posX < 10 && cell.posY > -1 && cell.posY < 10) {
                if (((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text == "") {
                    ((imageViews[cell.posY * 10 + cell.posX][0] as CardView)[0] as TextView).text =
                        "O"
                }
            }
        }
    }

    private fun getOurShips(): List<Ship> {
        return gameStartPack.ships
    }

    override fun onDestroy() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroy()
    }
}

enum class Turn {
    BOT,
    USER,
    NONE
}