package com.metalichecky.amonguseditor.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.repo.AssetRepo
import com.metalichecky.amonguseditor.repo.ItemsRepo
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SpaceBackgroundView : SurfaceView, SurfaceHolder.Callback {
    var drawThread: SpaceDrawHandler? = null
    var world: World? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, theme: Int) : super(context, attrs, theme) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        holder.addCallback(this)
        world = createWord()
    }

    private fun createWord(): World {
        return world ?: World()
    }

    override fun surfaceChanged(sh: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(sh: SurfaceHolder) {
        drawThread?.stop()
    }

    override fun surfaceCreated(sh: SurfaceHolder) {
        drawThread = SpaceDrawHandler(holder, createWord())
        drawThread?.start()
    }
}

class SpaceDrawHandler(
    val surfaceHolder: SurfaceHolder,
    var world: World
) : Runnable {
    companion object {
        const val FRAMERATE = 30f
        val FRAME_DURATION_MS = (1000f / FRAMERATE).roundToLong()
        val DEFAULT_SCREEN_SIZE = Size(500, 500)
    }

    var running = false
        private set

    var screenOffset = PointF()
    private var surfaceSize: Size = DEFAULT_SCREEN_SIZE

    init {
        val canvas = surfaceHolder.lockCanvas()
        surfaceSize = Size(canvas.width, canvas.height)
        surfaceHolder.unlockCanvasAndPost(canvas)
    }

    fun start() {
        running = true
        Thread(this).start()
    }

    fun stop() {
        running = false
    }

    override fun run() {
        while (running) {
            val updatingDuration = measureTimeMillis {
                surfaceHolder.lockCanvas()?.let {
                    update(it)
                    surfaceHolder.unlockCanvasAndPost(it)
                }
            }
            val delayDuration = FRAME_DURATION_MS - updatingDuration
            if (delayDuration > 0) {
                Thread.sleep(delayDuration)
            }
        }
    }

    private fun update(canvas: Canvas) {
        world.update()
        screenOffset.x = world.size.x / 2 - surfaceSize.width / 2
        screenOffset.y = world.size.y / 2 - surfaceSize.height / 2
        world.draw(canvas, screenOffset)
    }
}

class World() {
    companion object {
        val DEFAULT_SIZE = PointF(2000f, 2000f)
        const val STARS_MIN = 20
        const val STARS_MAX = 40
        const val HATS_MIN = 5
        const val HATS_MAX = 10
        const val PETS_MIN = 5
        const val PETS_MAX = 10
    }

    var size: PointF = DEFAULT_SIZE
    var backgroundColor: Int = Color.parseColor("#202020")
    var lastUpdateTimeMs: Long = -1
    var random = Random(System.currentTimeMillis())


    var objects: List<GameObject> = listOf()

    init {
        generate()
    }

    fun generate() {
        val objects = mutableListOf<GameObject>()

        val starsCount = random.nextInt(STARS_MIN, STARS_MAX)
        val stars = Array<StarObject>(starsCount) {
            StarObject().apply { }
        }
        val hatsCount = random.nextInt(HATS_MIN, HATS_MAX)
        val hats = Array<HatObject>(hatsCount) {
            HatObject().apply { }
        }
        val petsCount = random.nextInt(PETS_MIN, PETS_MAX)
        val pets = Array<PetObject>(petsCount) {
            PetObject().apply {}
        }
        objects.addAll(stars)
        objects.addAll(hats)
        objects.addAll(pets)

        objects.forEach {
            generateObject(it)
        }
        this.objects = objects
    }

    private fun generateObject(it: GameObject) {
        val center = PointF(size.x / 2f, size.y / 2f)
        val centerRadius = 300f
        it.generate()
        it.position.x = random.nextFloat() * size.x
        it.position.y = random.nextFloat() * size.y
        it.rotation = random.nextFloat(0f, 360f)
        if (it is StarObject) {
            it.direction.x = 1.0f
            it.direction.y = 0.0f
        } else {
            val randomCenter = PointF(
                center.x + random.nextSignedFloat() * centerRadius,
                center.y + random.nextSignedFloat() * centerRadius,
            )
            it.direction.x = randomCenter.x - it.position.x
            it.direction.y = randomCenter.y - it.position.y
            it.direction.normalize()
        }
    }

    fun update() {
        val currentTimeMs = System.currentTimeMillis()
        if (lastUpdateTimeMs < 0) {
            lastUpdateTimeMs = currentTimeMs
        }
        val deltaTimeMs = currentTimeMs - lastUpdateTimeMs
        objects.forEach {
            bounceObject(it)
            it.update(deltaTimeMs)
        }
        lastUpdateTimeMs = currentTimeMs
    }

    fun bounceObject(gameObject: GameObject) {
        if (gameObject.position.x !in 0f..size.x) {
            gameObject.direction.x = -gameObject.direction.x
        }
        if (gameObject.position.y !in 0f..size.y) {
            gameObject.direction.y = -gameObject.direction.y
        }
    }

    fun draw(canvas: Canvas, offset: PointF) {
        canvas.drawColor(backgroundColor)
        objects.forEach { gameObject ->
            gameObject.texture?.let { texture ->
                val positionX = gameObject.position.x - offset.x
                val positionY = gameObject.position.y - offset.y
                val positionMatrix = Matrix()
                positionMatrix.postRotate(
                    gameObject.rotation,
                    texture.width.toFloat() / 2f,
                    texture.height.toFloat() / 2f
                )
                positionMatrix.postTranslate(positionX, positionY)
                canvas.drawBitmap(
                    texture,
                    positionMatrix,
                    null
                )
            }
        }
    }
}

abstract class GameObject() {
    companion object {
        //speed is change per ms
        val random = Random(System.currentTimeMillis())
    }

    var texture: Bitmap? = null
    var position = PointF()
    var rotation: Float = 0f
    var speed: Float = 0f
    var direction = PointF(0f, 0f)
    var rotationSpeed: Float = 0f
    abstract var minSpeed: Float
    abstract var maxSpeed: Float
    abstract var minRotationSpeed: Float
    abstract var maxRotationSpeed: Float

    open fun generate() {
        speed = random.nextFloat(minSpeed, maxSpeed)
        rotationSpeed = random.nextFloat(minRotationSpeed, maxRotationSpeed)
    }

    open fun update(deltaTimeMs: Long) {
        position.x += (direction.x * speed * deltaTimeMs)
        position.y += (direction.y * speed * deltaTimeMs)
        rotation += rotationSpeed * deltaTimeMs
    }
}

class StarObject() : GameObject() {
    companion object {

    }

    override var minRotationSpeed: Float = 0f
    override var maxRotationSpeed: Float = 0f
    override var minSpeed: Float = 0f
    override var maxSpeed: Float = 0.008f

    init { }

    override fun generate() {
        super.generate()
        val sizeScale = random.nextFloat(0.2f, 1.2f)
        val starTexture = AssetRepo.getAssetBitmap("other/star.png") ?: return

        texture = Bitmap.createScaledBitmap(
            starTexture,
            (starTexture.width * sizeScale).roundToInt(),
            (starTexture.height * sizeScale).roundToInt(),
            false
        )
    }
}

class HatObject() : GameObject() {
    companion object {}

    override var minRotationSpeed: Float = 0.00f
    override var maxRotationSpeed: Float = 0.05f
    override var minSpeed: Float = 0.005f
    override var maxSpeed: Float = 0.05f

    init {
    }

    override fun generate() {
        super.generate()
        ItemsRepo.getRandomNonRepeatable(Hat::class)?.let {
            AssetRepo.getAssetBitmap(it)?.let { bitmap ->
                val dstSize = Point(
                    (bitmap.width * 3f).roundToInt(),
                    (bitmap.height * 3f).roundToInt()
                )
                texture = Bitmap.createScaledBitmap(bitmap, dstSize.x, dstSize.y, false)
            }
        }
    }
}

class PetObject() : GameObject() {
    companion object {}

    override var minRotationSpeed: Float = 0.00f
    override var maxRotationSpeed: Float = 0.05f
    override var minSpeed: Float = 0.005f
    override var maxSpeed: Float = 0.05f

    init {

    }

    override fun generate() {
        super.generate()
        ItemsRepo.getRandomNonRepeatable(Pet::class)?.let {
            AssetRepo.getAssetBitmap(it)?.let { bitmap ->
                val dstSize = Point(
                    (bitmap.width * 3f).roundToInt(),
                    (bitmap.height * 3f).roundToInt()
                )
                texture = Bitmap.createScaledBitmap(bitmap, dstSize.x, dstSize.y, false)
            }
        }
    }
}

fun Random.nextSignedFloat(): Float {
    return nextFloat() * 2f - 1f
}

fun Random.nextFloat(minValue: Float, maxValue: Float): Float {
    val interval = maxValue - minValue
    return minValue + nextFloat() * interval
}

fun PointF.normalize() {
    val length = this.length()
    this.x /= length
    this.y /= length
}