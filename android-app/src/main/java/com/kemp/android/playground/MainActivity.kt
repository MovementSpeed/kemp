package com.kemp.android.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kemp.android.app.AndroidApplication
import com.kemp.android.io.AndroidAssets
import com.kemp.android.scene.AndroidScene
import com.kemp.core.Kemp
import com.kemp.core.playground.PlaygroundGame
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playgroundGame = PlaygroundGame()

        val androidApplication = AndroidApplication(this) {
            playgroundGame.worldConfig(it)
        }

        ll_root.addView(androidApplication.view)

        val androidAssets = AndroidAssets(
            this,
            androidApplication.engine,
            androidApplication.assetLoader,
            androidApplication.resourceLoader)

        val androidScene = AndroidScene(androidApplication.scene, androidApplication.ecsCameraEntity)

        lifecycle.addObserver(androidApplication)

        Kemp.application = androidApplication
        Kemp.assets = androidAssets
        Kemp.scene = androidScene
        Kemp.game = playgroundGame
        Kemp.start()
    }
}