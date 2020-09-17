package com.kemp.core.app

/**
 * Platforms should implement their own application mechanism,
 * and callback updates to the core. The core doesn't know how
 * an application is implemented, it just needs to know it's
 * running, to update all core systems.
 */
interface Application {
    var update: (frameTimeNanos: Long) -> Unit
}