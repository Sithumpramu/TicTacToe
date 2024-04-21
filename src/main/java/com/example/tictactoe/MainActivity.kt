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


    fun creategame(){
       GameData.saveGameModel(
           GameModel(
               gameStatus = GameStatus.JOINED
               )
       )
      startgame()
    }

    fun startgame(){
        startActivity((Intent(this,GameActivity::class.java)))
    }
}

