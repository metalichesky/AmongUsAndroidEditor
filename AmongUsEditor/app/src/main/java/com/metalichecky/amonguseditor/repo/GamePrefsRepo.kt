package com.metalichecky.amonguseditor.repo

import com.metalichecky.amonguseditor.model.gameprefs.GamePrefs
import com.metalichecky.amonguseditor.util.GameFileUtils
import java.io.FileReader
import java.io.FileWriter
import java.util.*

object GamePrefsRepo {

    suspend fun isGamePrefsExists(): Boolean {
        return GameFileUtils.getAmongUsPrefsFile()?.exists() ?: false
    }

    suspend fun getGamePrefs(): GamePrefs? {
        val prefsFile = GameFileUtils.getAmongUsPrefsFile() ?: return null
        val reader = Scanner(prefsFile)
        var prefsString = ""
        if (reader.hasNextLine()) {
            prefsString = reader.nextLine()
        }
        reader.close()
        return GamePrefs.fromString(prefsString)
    }

    suspend fun saveGamePrefs(gamePrefs: GamePrefs) {
        val prefsFile = GameFileUtils.getAmongUsPrefsFile() ?: return
        val fileWriter = FileWriter(prefsFile)
        val prefsString = gamePrefs.toString()
        fileWriter.write(prefsString)
        fileWriter.flush()
        fileWriter.close()
    }
}