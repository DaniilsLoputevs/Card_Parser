package cardparser.scenario

import cardparser.CARD_WIDTH
import cardparser.STACK_GAP
import cardparser.ashley.logics.StackTouchLogic
import cardparser.ashley.systems.Debug
import cardparser.ashley.systems.DebugSystem
import cardparser.ashley.systems.RenderSystem
import cardparser.ashley.systems.TaskExecutorSystem
import cardparser.ashley.systems.input.screen.CardDragListenerNew
import cardparser.ashley.systems.input.screen.MainStackListenerNew
import cardparser.ashley.systems.input.screen.ScreenTouchSystem
import cardparser.asset.CardBackAtlas
import cardparser.asset.CardDeckAtlas
import cardparser.asset.GeneralAsset
import cardparser.entities.Card
import cardparser.entities.MainStack
import cardparser.entities.Stack
import cardparser.logger.loggerApp
import cardparser.tasks.CalculateTouchable
import cardparser.tasks.CardPosition
import cardparser.tasks.StackBinding
import cardparser.tasks.TaskManager
import cardparser.tasks.cancel.MainStackTouchCancel
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage

private lateinit var mainStack: MainStack
private lateinit var subStack: MainStack
private lateinit var upStacks: MutableList<Stack>
private lateinit var bottomStacks: MutableList<Stack>

private lateinit var upSubStacks: MutableList<Stack>
private lateinit var botUpStacks: MutableList<Stack>
private lateinit var botUpSubStacks: MutableList<Stack>
private lateinit var allStacks: MutableList<Stack>

private lateinit var cards: MutableList<Card>


fun Engine.initKlondikeGame(assets: AssetStorage) {
    this.run {
        mainStack = createMasterStack(assets)
        subStack = createSubStack(assets)
        upStacks = createUpStacks(assets)
        bottomStacks = createBottomStacks(assets)

        upSubStacks = (upStacks + subStack) as MutableList<Stack>
        botUpStacks = (bottomStacks + upStacks) as MutableList<Stack>
        botUpSubStacks = (bottomStacks + upStacks + subStack) as MutableList<Stack>
        allStacks = (bottomStacks + upStacks + mainStack + subStack) as MutableList<Stack>

        cards = createCardDeck(assets[CardDeckAtlas.CARD_DECK_DEFAULT.desc], mainStack.pos())
                .shuffleCards().onEachIndexed { index, card -> card.setPos(z = index.toFloat()) }

        /* Debug environment init */
        Debug.cards = cards
        Debug.mainStack = mainStack
        Debug.subStack = subStack
        Debug.bottomStacks = bottomStacks
        Debug.upStacks = upStacks

        /* Task's environment init */
        StackBinding.botUpStacks = botUpStacks
        CalculateTouchable.botUpStacks = botUpStacks
        CalculateTouchable.touchTouchLogic = StackTouchLogic.KLONDIKE
        CardPosition.allStacks = allStacks
        MainStackTouchCancel.mainStack = mainStack
        MainStackTouchCancel.subStack = subStack

        /* Systems init */
        getSystem<DebugSystem>().apply { setProcessing(true) }
        getSystem<ScreenTouchSystem>().apply {
            setProcessing(true)
            addListeners { cursor ->
                setOf(
                        CardDragListenerNew(botUpStacks, cursor),
                        MainStackListenerNew(mainStack, subStack, cursor)
                )
            }
        }
        getSystem<TaskExecutorSystem>().apply { setProcessing(true) }
        getSystem<RenderSystem>().apply {
            this.setBackground(assets[GeneralAsset.BACKGROUND_DEFAULT.desc])
//            this.configCardBack(assets[CardBackAtlas.CARD_BACK_RED_DEFAULT.desc].findRegion("red_deck"))
            this.setCardBack(assets[CardBackAtlas.CARD_BACK_DEFAULT.desc].findRegion("dark"))
            setProcessing(true)
        }

        // start game
        startKlondikeGame()
        TaskManager.commit(CalculateTouchable(TaskManager.genId()))

//        Debug.act("F", "LAST BOT STACK") { logger.debug("bot", bottomStacks.last().cards())}
    }
}

fun startKlondikeGame() {
    // add all cards to Main Stack
    mainStack.addAll(cards)

    // throw cards to Bottom Stacks
    repeat(7) {
        bottomStacks.forEachIndexed { index, stack ->
            val cards = stack.cards()
            if (cards.size < (index + 1)) {
                mainStack.cards().removeLastOrNull()?.let { stack.add(it) }
                if (cards.size == (index + 1)) cards.last().apply { touchable(true); open(true) }
            }
        }
    }
}


private fun Engine.createBottomStacks(assets: AssetStorage): MutableList<Stack> {
    return createStackRaw(assets, 7, 31f, 290f, STACK_GAP, this::createBottomStack)
}

private fun Engine.createUpStacks(assets: AssetStorage): MutableList<Stack> {
    val initX = STACK_GAP * 3 + CARD_WIDTH * 3 + 31f
    return createStackRaw(assets, 4, initX, 520f, STACK_GAP, this::createUpStack)
}

private fun Engine.createMasterStack(assets: AssetStorage): MainStack {
    return createMainStack(assets[GeneralAsset.CARD_STACK.desc], STACK_GAP + 31f, 520f, _order = 0, isMain = true)
}

private fun Engine.createSubStack(assets: AssetStorage): MainStack {
    return createMainStack(assets[GeneralAsset.CARD_STACK.desc],
            STACK_GAP * 2 + CARD_WIDTH + 31f, 520f, _order = 1, isMain = false
    )
}

private fun MutableList<Card>.shuffleCards(): MutableList<Card> = this.apply { shuffle(MathUtils.random) }

private val logger by lazy { loggerApp<InitScenarioKlondike>() }

class InitScenarioKlondike