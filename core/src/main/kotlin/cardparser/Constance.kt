package cardparser

const val GAME_TITLE = "Card Parser by Daniils & Maksim. ))))"

const val WORLD_WIDTH_WU = 1280f // world units
const val WORLD_HEIGHT_WU = 720f // world units

const val CARD_WIDTH = 128f // world units     --    137f
const val CARD_HEIGHT = 192f // world units    --    192f

const val STACK_OPEN_CARD_OFFSET = 30f // world units

/**
 * how much less the offset for closed cards.
 * 1 == same[STACK_OPEN_CARD_OFFSET]  ||  2 == two times less
 */
const val STACK_CLOSE_CARD_OFFSET = 1.5 // times

const val STACK_GAP = 40f // world units

const val MIN_CARD_MOVE_SPEED_RATE = 0.15f
const val MAX_CARD_MOVE_SPEED_RATE = MIN_CARD_MOVE_SPEED_RATE * 1.50f

/**
 * If card's pos is different more that (this const) from requirment card's pos,
 * card moves by [MAX_CARD_MOVE_SPEED_RATE] speed
 */
const val DISTANCE_CARD_MOVE_SLOW = 90F // world units

@Deprecated("use in old system. For remove")
const val STACK_START_SPEED = 0.00000000001f // percents 0.00f - 1.00f (default 0.15)

@Deprecated("not in use")
const val GAME_START_DELAY = 1f // ? second || percents 0.00f - 1.00f

/**
 * The radius from the starting position of the touch
 * at which the touch meaning a touch, not a dragging.
 */
const val TOUCH_RANGE = 3f // world units

/** Time to make second touch after first touch for get double touch. */
const val DOUBLE_TOUCH_TIMER = 0.75f

/** [cardparser.tasks.GameActionHistory] */
const val ACTION_HISTORY_SIZE = 25 // actions


/* Simon part */

//const val PREFERENCE_MUSIC_ENABLED_KEY = "musicEnabled"
//const val PREFERENCE_HIGHSCORE_KEY = "highScore"
