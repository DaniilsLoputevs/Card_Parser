package cardparser.screens

import cardparser.MainGame
import cardparser.utils.AnglesButtonStyle
import cardparser.utils.ImageButtonStyle
import cardparser.utils.LabelStyles
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.scene2d.*

enum class GamesList(val game: String) {
    KLONDIKE_GAME("Klondike"),
    SPIDER_GAME("Spider"),
}

class MenuScreen(game: MainGame) : AbstractScreen(game) {

    private lateinit var startStack: KStack

    override fun show() {
        stage.isDebugAll = false
        showMainMenu()
        super.show()
    }

    override fun render(delta: Float) {
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    private fun showMainMenu() {
        stage.clear()
        stage.actors {
            stack {
                setBounds(260f, 360f, 360f, 360f)
                imageButton(AnglesButtonStyle.LEFTUP.name) {
                    setFillParent(true)
                }
                label("Start \n game", LabelStyles.WHITE46_BI.name) {
                    setAlignment(Align.center)
                }
                onClick {
                    showGameList()
                }
            }
            stack {
                setBounds(660f, 360f, 360f, 360f)
                imageButton(AnglesButtonStyle.RIGHTUP.name) {
                    setFillParent(true)
                }
                label("Hall \n of fame", LabelStyles.WHITE46_BI.name) {
                    setAlignment(Align.center)
                }
            }
            stack {
                setBounds(260f, 1f, 360f, 360f)
                imageButton(AnglesButtonStyle.LEFTDOWN.name) {
                    setFillParent(true)
                }
                label("Options", LabelStyles.WHITE46_BI.name) {
                    setAlignment(Align.center)
                }
            }
            stack {
                setBounds(660f, 1f, 360f, 360f)
                imageButton(AnglesButtonStyle.RIGHTDOWN.name) {
                    setFillParent(true)
                }
                label("Quit", LabelStyles.WHITE46_BI.name) {
                    setAlignment(Align.center)
                }
                onClick {
                    dispose()
                    Gdx.app.exit()
                }
            }
        }
    }

    private fun showGameList() {
        stage.clear()
        stage.actors {
            table {
                GamesList.values().forEach { elem ->
                    label(elem.game, LabelStyles.WHITE46_BI.name) {
                        setAlignment(Align.center)
                        onClick {
                            game.addScreen(GameScreen(game).apply {
                                chosen = elem.game
                            })
                            game.setScreen<GameScreen>()
                        }
                    }
                    this.row()
                }
                setFillParent(true)
                pack()
            }
        }
    }
}
