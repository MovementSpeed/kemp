package com.kemp.android.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.kemp.android.app.AndroidApplication
import com.kemp.android.input.AndroidKeyboardInput
import com.kemp.android.input.AndroidTouchInput
import com.kemp.android.io.AndroidAssets
import com.kemp.android.scene.AndroidScene
import com.kemp.core.Kemp
import com.kemp.core.playground.PlaygroundGame
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val keyboardInput = AndroidKeyboardInput()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playgroundGame = PlaygroundGame()

        val androidApplication = AndroidApplication(this) {
            playgroundGame.worldConfig(it)
        }

        ll_root.addView(androidApplication.view)
        val touchInput = AndroidTouchInput(androidApplication.view)

        val androidAssets = AndroidAssets(
            this,
            androidApplication.engine,
            androidApplication.assetLoader,
            androidApplication.resourceLoader)

        val androidScene = AndroidScene(androidApplication.scene, androidApplication.ecsCameraEntity)

        lifecycle.addObserver(androidApplication)

        Kemp.application = androidApplication
        Kemp.touchInput = touchInput
        Kemp.keyboardInput = keyboardInput
        Kemp.assets = androidAssets
        Kemp.scene = androidScene
        Kemp.game = playgroundGame
        Kemp.start()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return keyboardInput.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyboardInput.onKeyDown(keyCode, event)
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        return keyboardInput.onKeyMultiple(keyCode, repeatCount, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        return keyboardInput.onKeyLongPress(keyCode, event)
    }
}