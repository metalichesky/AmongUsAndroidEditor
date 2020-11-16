package com.metalichecky.amonguseditor.model.gameprefs

class GamePrefs() {
    companion object {
        const val PARAMS_SEPARATOR = ","

        val IDX_LAST_PLAYER_NAME = 0
        val IDX_TOUCH_CONFIG = 1
        val IDX_COLOR_CONFIG = 2
        val IDX_IGNORED_UNKNOWN = 3
        val IDX_IGNORED_SEND_NAME = 4
        val IDX_IGNORED_SEND_TELEMETRY = 5
        val IDX_IGNORED_SEND_DATA_SCREEN = 6
        val IDX_SHOW_ADS_SCREEN = 7
        val IDX_SHOW_MIN_PLAYER_WARNING = 8
        val IDX_SHOW_ONLINE_HELP = 9
        val IDX_LAST_HAT = 10
        val IDX_SFX_VOLUME = 11
        val IDX_MUSIC_VOLUME = 12
        val IDX_JOYSTICK_SIZE = 13
        val IDX_LAST_GAME_START = 14
        val IDX_LAST_SKIN = 15
        val IDX_LAST_PET = 16
        val IDX_CENSOR_CHAT = 17
        val IDX_LAST_LANGUAGE = 18
        val IDX_VSYNC = 19

        val DEFAULT_LAST_PLAYER_NAME = ""
        val DEFAULT_TOUCH_CONFIG = 0
        val DEFAULT_COLOR_CONFIG = 0
        val DEFAULT_IGNORED_UNKNOWN = 1
        val DEFAULT_IGNORED_SEND_NAME = false
        val DEFAULT_IGNORED_SEND_TELEMETRY = false
        val DEFAULT_IGNORED_SEND_DATA_SCREEN = false
        val DEFAULT_SHOW_ADS_SCREEN = 0x01
        val DEFAULT_SHOW_MIN_PLAYER_WARNING = true
        val DEFAULT_SHOW_ONLINE_HELP = false
        val DEFAULT_LAST_HAT = 0
        val DEFAULT_SFX_VOLUME = 255
        val DEFAULT_MUSIC_VOLUME = 255
        val DEFAULT_JOYSTICK_SIZE = 1.0f
        val DEFAULT_LAST_GAME_START = 0L
        val DEFAULT_LAST_SKIN = 0
        val DEFAULT_LAST_PET = 0
        val DEFAULT_CENSOR_CHAT = false
        val DEFAULT_LAST_LANGUAGE = 0
        val DEFAULT_VSYNC = false

        fun fromString(prefsString: String): GamePrefs {
            return GamePrefs().apply {
                val paramsStrings = prefsString.split(PARAMS_SEPARATOR)
                paramsStrings.forEachIndexed { idx, value ->
                    this.paramsMap[idx] = value
                }
            }
        }
    }

    var lastPlayerName: String //nickname
    get() {
        return paramsMap[IDX_LAST_PLAYER_NAME] ?: DEFAULT_LAST_PLAYER_NAME
    }
    set(value) {
        paramsMap[IDX_LAST_PLAYER_NAME] = value
    }

    var touchConfig: Int //controls: 0 - joystick, 1 - touch
        get() {
            return paramsMap[IDX_TOUCH_CONFIG]?.toInt() ?: DEFAULT_TOUCH_CONFIG
        }
        set(value) {
            paramsMap[IDX_TOUCH_CONFIG] = value.toString()
        }

    var colorConfig: Int //0-11 is normal, 12+ is fortegreen
        get() {
            return paramsMap[IDX_COLOR_CONFIG]?.toInt() ?: DEFAULT_COLOR_CONFIG
        }
        set(value) {
            paramsMap[IDX_COLOR_CONFIG] = value.toString()
        }

    var ignoredUnknown: Int //default 1
        get() {
            return paramsMap[IDX_IGNORED_UNKNOWN]?.toInt() ?: DEFAULT_IGNORED_UNKNOWN
        }
        set(value) {
            paramsMap[IDX_IGNORED_UNKNOWN] = value.toString()
        }

    var ignoredSendName: Boolean //default false
        get() {
            return paramsMap[IDX_IGNORED_SEND_NAME]?.toBoolean() ?: DEFAULT_IGNORED_SEND_NAME
        }
        set(value) {
            paramsMap[IDX_IGNORED_SEND_NAME] = value.toString()
        }

    var ignoredSendTelemetry: Boolean //default false
        get() {
            return paramsMap[IDX_IGNORED_SEND_TELEMETRY]?.toBoolean() ?: DEFAULT_IGNORED_SEND_TELEMETRY
        }
        set(value) {
            paramsMap[IDX_IGNORED_SEND_TELEMETRY] = value.toString()
        }

    var ignoredSendDataScreen: Boolean //default false
        get() {
            return paramsMap[IDX_IGNORED_SEND_DATA_SCREEN]?.toBoolean() ?: DEFAULT_IGNORED_SEND_DATA_SCREEN
        }
        set(value) {
            paramsMap[IDX_IGNORED_SEND_DATA_SCREEN] = value.toString()
        }

    var showAdsScreen: Int //Personalized 0x00, NonPersonalized 0x01, Accepted 0x80, Purchased 0x82
        get() {
            return paramsMap[IDX_SHOW_ADS_SCREEN]?.toInt() ?: DEFAULT_SHOW_ADS_SCREEN
        }
        set(value) {
            paramsMap[IDX_SHOW_ADS_SCREEN] = value.toString()
        }

    var showMinPlayerWarning: Boolean //
        get() {
            return paramsMap[IDX_SHOW_MIN_PLAYER_WARNING]?.toBoolean() ?: DEFAULT_SHOW_MIN_PLAYER_WARNING
        }
        set(value) {
            paramsMap[IDX_SHOW_MIN_PLAYER_WARNING] = value.toString()
        }

    var showOnlineHelp: Boolean //
        get() {
            return paramsMap[IDX_SHOW_ONLINE_HELP]?.toBoolean() ?: DEFAULT_SHOW_ONLINE_HELP
        }
        set(value) {
            paramsMap[IDX_SHOW_ONLINE_HELP] = value.toString()
        }

    var lastHat: Int //hat number
        get() {
            return paramsMap[IDX_LAST_HAT]?.toInt() ?: DEFAULT_LAST_HAT
        }
        set(value) {
            paramsMap[IDX_LAST_HAT] = value.toString()
        }

    var sfxVolume: Int //0 - 255
        get() {
            return paramsMap[IDX_SFX_VOLUME]?.toInt() ?: DEFAULT_SFX_VOLUME
        }
        set(value) {
            paramsMap[IDX_SFX_VOLUME] = value.toString()
        }

    var musicVolume: Int //0 - 255
        get() {
            return paramsMap[IDX_MUSIC_VOLUME]?.toInt() ?: DEFAULT_MUSIC_VOLUME
        }
        set(value) {
            paramsMap[IDX_MUSIC_VOLUME] = value.toString()
        }

    var joyStickSize: Float //0.5, 1, 1.5
        get() {
            return paramsMap[IDX_JOYSTICK_SIZE]?.toFloat() ?: DEFAULT_JOYSTICK_SIZE
        }
        set(value) {
            paramsMap[IDX_JOYSTICK_SIZE] = value.toString()
        }

    var lastGameStart: Long //Ticks since last game started. Used to check if left game too early
        get() {
            return paramsMap[IDX_LAST_GAME_START]?.toLong() ?: DEFAULT_LAST_GAME_START
        }
        set(value) {
            paramsMap[IDX_LAST_GAME_START] = value.toString()
        }

    var lastSkin: Int //0 - 15
        get() {
            return paramsMap[IDX_LAST_SKIN]?.toInt() ?: DEFAULT_LAST_SKIN
        }
        set(value) {
            paramsMap[IDX_LAST_SKIN] = value.toString()
        }

    var lastPet: Int //0 - 10
        get() {
            return paramsMap[IDX_LAST_PET]?.toInt() ?: DEFAULT_LAST_PET
        }
        set(value) {
            paramsMap[IDX_LAST_PET] = value.toString()
        }

    var censorChat: Boolean
        get() {
            return paramsMap[IDX_CENSOR_CHAT]?.toBoolean() ?: DEFAULT_CENSOR_CHAT
        }
        set(value) {
            paramsMap[IDX_CENSOR_CHAT] = value.toString()
        }

    var lastLanguage: Int // 0-4, 0: English, 1: Spanish, 2: Portuguese, 3: Korean, 4: Russian
        get() {
            return paramsMap[IDX_LAST_LANGUAGE]?.toInt() ?: DEFAULT_LAST_LANGUAGE
        }
        set(value) {
            paramsMap[IDX_LAST_LANGUAGE] = value.toString()
        }

    var vsync: Boolean
        get() {
            return paramsMap[IDX_VSYNC]?.toBoolean() ?: DEFAULT_VSYNC
        }
        set(value) {
            paramsMap[IDX_VSYNC] = value.toString()
        }

    private val paramsMap = mutableMapOf<Int, String>()

    fun copyFromString(prefsString: String) {
        val paramsStrings = prefsString.split(PARAMS_SEPARATOR)
        paramsStrings.forEachIndexed {idx, value ->
            paramsMap[idx] = value
        }
    }

    override fun toString(): String {
        val params = paramsMap.toSortedMap().map {
            it.value
        }
        return params.joinToString(PARAMS_SEPARATOR)
    }
}