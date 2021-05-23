package com.metalichecky.amonguseditor.repo

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.metalichecky.amonguseditor.model.gameprefs.GamePrefs
import com.metalichecky.amonguseditor.util.CatchedGamePrefsException
import com.metalichecky.amonguseditor.util.GameFileUtils
import java.io.FileWriter
import java.util.*

object GamePrefsRepo {

    suspend fun isGamePrefsExists(): Boolean {
        return GameFileUtils.getAmongUsPrefsFile()?.exists() ?: false
    }

    suspend fun getGamePrefs(): GamePrefs? {
        return try {
            val prefsFile = GameFileUtils.getAmongUsPrefsFile()
            val reader = Scanner(prefsFile)
            reader ?: return null
            var prefsString = ""
            if (reader.hasNextLine()) {
                prefsString = reader.nextLine()
            }
            reader.close()
            GamePrefs.fromString(prefsString)
        } catch (ex: Exception) {
            Firebase.crashlytics.recordException(CatchedGamePrefsException.create(ex))
            null
        }
    }

    suspend fun saveGamePrefs(gamePrefs: GamePrefs) {
        try {
            val prefsFile = GameFileUtils.getAmongUsPrefsFile() ?: return
            val fileWriter = FileWriter(prefsFile)
            val prefsString = gamePrefs.toString()
            fileWriter.write(prefsString)
            fileWriter.flush()
            fileWriter.close()
        } catch (ex: Exception) {
            Firebase.crashlytics.recordException(CatchedGamePrefsException.create(ex))
        }
    }
}