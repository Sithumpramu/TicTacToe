package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playbtn.setOnClickListener{
            creategame()
        }
    }



    fun creategame() {
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )

        // Start the game with player names
        val player1Name = binding.player1.text.toString()
        val player2Name = binding.player2.text.toString()

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("PLAYER_ONE_NAME", player1Name)
            putExtra("PLAYER_TWO_NAME", player2Name)
        }

        startActivity(intent)
    }
}

