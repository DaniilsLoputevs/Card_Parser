package cardparser.screens

import cardparser.MainGame
import cardparser.utils.ImageButtonStyle
import cardparser.utils.LabelStyles
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.scene2d.*

class MenuScreen(game: MainGame) : AbstractScreen(game) {

    private lateinit var startStack: KStack

    override fun show() {
        stage.isDebugAll = false
        showMainMenu()
        super.show()
    }

    override fun render(delta: Float) {
        clearScreen(255f, 255f, 255f, 0.5f)
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
                imageButton(ImageButtonStyle.SPADES.name) {
                    setFillParent(true)
                }
                label("Start game", LabelStyles.WHITE32.name) {
                    setAlignment(Align.center)
                }
                onClick {
                    showGameList()
                }
            }
            stack {
                setBounds(660f, 360f, 360f, 360f)
                imageButton(ImageButtonStyle.HEARTS.name) {
                    setFillParent(true)
                }
                label("Achievements", LabelStyles.BLACK32.name) {
                    setAlignment(Align.center)
                }
            }
            stack {
                setBounds(260f, 1f, 360f, 360f)
                imageButton(ImageButtonStyle.DIAMONDS.name) {
                    setFillParent(true)
                }
                label("Options", LabelStyles.BLACK32.name) {
                    setAlignment(Align.center)
                }
            }
            stack {
                setBounds(660f, 1f, 360f, 360f)
                imageButton(ImageButtonStyle.CLUBS.name) {
                    setFillParent(true)
                }
                label("Quit", LabelStyles.WHITE32.name) {
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
                label("Klondike Game", LabelStyles.BLACK32.name) {
                    setAlignment(Align.center)
                    onClick {
                        game.addScreen(GameScreen(game).apply {
                            chosen = "Klondike Game"
                        })
                        game.setScreen<GameScreen>()
                    }
                }
                setFillParent(true)
                pack()
            }
        }
    }
}
