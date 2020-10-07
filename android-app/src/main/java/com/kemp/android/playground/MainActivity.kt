package com.kemp.android.playground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kemp.android.androidCreate
import com.kemp.core.playground.PlaygroundGame
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playgroundGame = PlaygroundGame()
        val view = androidCreate(this, lifecycle, playgroundGame)
        ll_root.addView(view)
    }
}