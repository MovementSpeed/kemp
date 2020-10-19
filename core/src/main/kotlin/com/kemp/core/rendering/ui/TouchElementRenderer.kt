package com.kemp.core.rendering.ui

import com.kemp.core.input.touch.TouchElement

interface TouchElementRenderer {
    fun render(renderer2D: Renderer2D, touchElement: TouchElement)
}