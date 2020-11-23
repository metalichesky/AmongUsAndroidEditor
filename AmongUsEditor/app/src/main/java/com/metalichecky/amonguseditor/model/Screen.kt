package com.metalichecky.amonguseditor.model

class Screen (
    val name: String = ""
) {
    private var startTimeMs: Long = -1L
    private val spentDurationMs: Long
    get() {
        return if (startTimeMs > 0) {
            System.currentTimeMillis() - startTimeMs
        } else {
            0L
        }
    }

    fun start() {
        startTimeMs = System.currentTimeMillis()
    }

    fun getSpentDurationSec(): Long {
        return spentDurationMs / 1000L
    }

}

enum class NavigateType(val typeName: String) {
    CLOSE("close"),
    OPEN("open")
}