package com.metalichecky.amonguseditor.util

import android.os.Build
import com.metalichecky.amonguseditor.repo.DataStore
import java.io.File

object GameFileUtils {
    const val GAME_FILE_APP_DIR = "game_dir"
    const val GAME_FILE_FILES_DIR = "game_files_dir"
    const val GAME_FILE_PREFS_FILE = "game_player_prefs_file"

    const val AMONG_US_FILES_DIR_NAME = "files"
    const val AMONG_US_PREFS_FILENAME = "playerPrefs"

    suspend fun getAmongUsAppDir(): File? {
        val settings = DataStore.Settings()
        // restore game dir from storage
        val amongUsCachedAppDirPath = settings.getGameAppDir()
        var amongUsCachedAppDir = if (amongUsCachedAppDirPath != null) {
            File(amongUsCachedAppDirPath)
        } else {
            null
        }
        if (amongUsCachedAppDir == null || !amongUsCachedAppDir.exists()) {
            // search game dir if we have not already founded
            amongUsCachedAppDir = searchAmongUsAppDir()
            if (amongUsCachedAppDir != null && amongUsCachedAppDir.exists()) {
                // if we found game dir, save it
                settings.setGameAppDir(amongUsCachedAppDir.absolutePath)
            }
        }
        FirebaseLogger.logFile(
            amongUsCachedAppDir?.path ?: "",
            GAME_FILE_APP_DIR,
            amongUsCachedAppDir?.exists() ?: false
        )
        return amongUsCachedAppDir
    }

    suspend fun getAmongUsFilesDir(): File? {
        val amongUsAppDir = getAmongUsAppDir()
        val amongUsFilesDir = if (amongUsAppDir != null) {
            File(amongUsAppDir, AMONG_US_FILES_DIR_NAME)
        } else {
            null
        }
        return amongUsFilesDir
    }

    suspend fun getAmongUsPrefsFile(): File? {
        val amongUsFilesDir = getAmongUsFilesDir()
        val amongUsPrefsFile = if (amongUsFilesDir != null) {
            File(amongUsFilesDir, AMONG_US_PREFS_FILENAME)
        } else {
            null
        }
        return amongUsPrefsFile
    }

    suspend fun isAmongUsAppExists(): Boolean {
        return getAmongUsAppDir()?.exists() ?: false
    }

    private fun getAmongUsPrefsSearchPath(): String {
        return Constants.AMONG_US_PACKAGE_NAME + File.separator +
                AMONG_US_FILES_DIR_NAME + File.separator +
                AMONG_US_PREFS_FILENAME
    }

    private fun searchAmongUsAppDir(): File? {
        var appDir: File? = FileUtils.getAppExternalDataDir(Constants.AMONG_US_PACKAGE_NAME)
        // get default app path
        val defaultPathExists = appDir != null && appDir.exists()
        if (!defaultPathExists && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // if default app path not exists, we should scan for it on device
            val pathToSearch = getAmongUsPrefsSearchPath()
            val externalRoot = FileUtils.getExternalDir()
            var finded: File? =
                FileUtils.searchInDir(externalRoot?.toPath(), pathToSearch)?.toFile()
            if (finded == null) {
                val sdCardRoot = FileUtils.getExternalSDCardDir()
                finded = FileUtils.searchInDir(sdCardRoot?.toPath(), pathToSearch)?.toFile()
            }
            appDir = finded?.parentFile?.parentFile
        }
        return appDir
    }
}