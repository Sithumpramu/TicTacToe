package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.databinding.ActivityGameBinding
import android.os.SystemClock
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

class GameActivity : AppCompatActivity(),View.OnClickListener{

    lateinit var binding : ActivityGameBinding

    private  var gameModel:GameModel? = null

//for fastest time
    private var startTime: Long = 0
    companion object {
        const val SHARED_PREF_NAME = "TicTacToePrefs"
        const val FASTEST_WINNING_TIME_KEY = "FastestWinningTime"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerOneName = intent.getStringExtra("PLAYER_ONE_NAME") ?: "Player One"
        val playerTwoName = intent.getStringExtra("PLAYER_TWO_NAME") ?: "Player Two"



        // Update UI elements with the retrieved names
        binding.user1.text = playerOneName
        binding.user2.text = playerTwoName

        


        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)


        binding.startgamebtn.setOnClickListener{
            startGame()

        }


        GameData.gameModel.observe(this){
            gameModel= it
            setUI()
        }


    }

    fun setUI(){
        gameModel?.apply {
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            binding.startgamebtn.visibility = View.VISIBLE

            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED -> {
                        binding.startgamebtn.visibility = View.INVISIBLE
                        "Game ID :"+ gameId
                    }
                    GameStatus.JOINED ->{
                        "Click on start game"
                    }
                    GameStatus.INPROGRESS ->{
                        binding.startgamebtn.visibility = View.INVISIBLE
                        currentPlayer + " turn"
                    }
                    GameStatus.FINISHED ->{
                        if(winner.isNotEmpty()) winner + " Won"
                        else "DRAW"
                    }
                }

        }
    }

    fun startGame(){
        gameModel?.apply {
            startTime = System.currentTimeMillis()
            updateGameData(
                GameModel(
                    gameId = gameId,
                    gameStatus = GameStatus.INPROGRESS
                )
            )


        }

    }

    fun updateGameData(model: GameModel){
        GameData.saveGameModel(model)
    }

    fun checkforWinner(){
        val WinningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.apply {
            var gameWon = false
            for(i in WinningPos){
                if(
                    filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]] == filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ){
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]

                    gameWon = true
                    break
                }
            }

            if (gameWon) {
                val endTime = System.currentTimeMillis() // Record the end time
                val duration = endTime - startTime // Calculate the game's duration

                // Check for new record
                val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
                val fastestTime = sharedPreferences.getLong(FASTEST_WINNING_TIME_KEY, Long.MAX_VALUE)

                if (duration < fastestTime) {
                    // New record, show a popup
                    sharedPreferences.edit()
                        .putLong(FASTEST_WINNING_TIME_KEY, duration) // Store the new record
                        .apply()

                    val durationInSeconds = duration / 1000.0
                    val previousBestInSeconds = fastestTime / 1000.0
                    showPopup("New Fastest Winning Time: $durationInSeconds seconds.  Previous Best: $previousBestInSeconds seconds")
                }

                updateGameData(this)
            }

            if(filledPos.none(){it.isEmpty()}){
                gameStatus = GameStatus.FINISHED
            }

        }
    }


    fun showPopup(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("New Record")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()

        alertDialog.show()
    }



    override fun onClick(v: View?) {
        gameModel?.apply {
            if(gameStatus!= GameStatus.INPROGRESS){
                Toast.makeText(applicationContext, "Game not started", Toast.LENGTH_SHORT).show()
                return
            }

            val clickedPos = (v?.tag as String).toInt()
            if(filledPos[clickedPos].isEmpty()){
                filledPos[clickedPos]= currentPlayer
                currentPlayer= if(currentPlayer=="X") "O" else "X"
                checkforWinner()
                updateGameData(this)


            }

        }
    }
}