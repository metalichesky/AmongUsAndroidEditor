package com.metalichecky.amonguseditor

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.metalichecky.amonguseditor.ui.SpaceDrawHandler
import com.metalichecky.amonguseditor.ui.World

class SpaceWallpaperService : WallpaperService() {
    var spaceWallpaperEngine: SpaceWallpaperServiceEngine? = null
    override fun onCreateEngine(): Engine {
        return SpaceWallpaperServiceEngine().apply {
            spaceWallpaperEngine = this
        }
    }

    inner class SpaceWallpaperServiceEngine : WallpaperService.Engine() {
        var drawThread: SpaceDrawHandler? = null
        var world: World? = null

        private fun createWord(): World {
            return world ?: World()
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            holder?.let {
                drawThread = SpaceDrawHandler(holder, createWord())
                drawThread?.start()
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            drawThread?.stop()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
        }
    }
}

