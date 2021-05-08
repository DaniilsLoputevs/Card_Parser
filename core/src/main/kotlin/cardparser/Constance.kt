package cardparser

const val V_WORLD_WIDTH_UNITS = 1280f // world units
const val V_WORLD_HEIGHT_UNITS = 720f // world units

const val CARD_STACK_OFFSET = 30f // world units

const val CARD_WIDTH = 137f // world units
const val CARD_HEIGHT = 192f // world units

const val STACK_GAP_STEP = 40f

const val MIN_SPEED_RATE = 0.15f
const val MAX_SPEED_RATE = MIN_SPEED_RATE * 1.75f

/** Ржака константы!!! гы-гы-гы! */
//const val MIN_SPEED_RATE = 0.15f
//const val MAX_SPEED_RATE = MIN_SPEED_RATE * 175f

//const val STACK_START_SPEED = 0.15f
const val STACK_START_SPEED = 0.00000000001f // percents 0.00f - 1.00f
/**
 * The radius from the starting position of the touch
 * at which the touch meaning a touch, not a dragging.
 */
const val TOUCH_RANGE = 3f // world units

const val DOUBLE_TOUCH_TIME_WINDOW = 200f // milliseconds

/* Simon part */

//const val UNIT_SCALE = 1 / 64f //   1/8 == 0.125   ||   1/64f == 0,015625   || 1/128 == 0,0078125

//const val PREFERENCE_MUSIC_ENABLED_KEY = "musicEnabled"
//const val PREFERENCE_HIGHSCORE_KEY = "highScore"
