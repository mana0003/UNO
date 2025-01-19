package view

//import UNO.images.*
import controller.controllerComponent.IUnoController
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes.*
import util.{Event, Observer}
import util.Event.*
//import model.*
import scalafx.scene.control.{ListCell, ListView}
import javafx.util.Callback
import model.gameComponent.{IPlayer, IUnoField, IPlayerHand}
import model.cardComponent.{ICard, cardColors, cardValues}
import model.cardComponent.cardIm.Card
import scalafx.scene.image.{Image, ImageView}

trait State {
  def display(pane: Pane): Unit
}

class BeginState(gui: UnoGUI, controller: IUnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()

    val logoImage = new ImageView(new Image(getClass.getResource("/extras/Logo.png").toString)) {
      fitHeight = 200
      fitWidth = 300
      preserveRatio = true
    }

    val startButtonImage = new ImageView(new Image(getClass.getResource("/extras/StartButton.png").toString)) {
      fitHeight = 120
      fitWidth = 300
      preserveRatio = true
    }

    val startButton = new scalafx.scene.control.Button {
      graphic = startButtonImage
      onAction = _ => {
        gui.setState(new GameState(gui, controller))
        gui.display()
      }
      style = "-fx-background-color: transparent;"
    }

    val loadButton = new scalafx.scene.control.Button("Load Game") {
      onAction = _ => {
        controller.loadGame()
        gui.setState(new GameState(gui, controller))
        gui.display()
      }
    }

    val vbox = new VBox {
      alignment = scalafx.geometry.Pos.Center
      spacing = 20
      children = Seq(logoImage, startButton, loadButton)
    }
    vbox.layoutX <== pane.width / 2 - vbox.width / 2
    vbox.layoutY <== pane.height / 2 - vbox.height / 2
    pane.children.add(vbox)
  }
}
// Update the GameState class to use images for the cards
class GameState(gui: UnoGUI, controller: IUnoController) extends State {
  override def display(pane: Pane): Unit = {
    println("Inside GameState display")
    pane.children.clear()

    val playerLabel = new scalafx.scene.control.Label(s"Current player: Player ${controller.field.currentPlayer + 1}")
    val topCardLabel = controller.getChosenColor match {
      case Some(color) =>
        println(s"Chosen color: $color")
        new scalafx.scene.control.Label(s"Your opponent chose the color: $color") {
          textFill = color match {
            case cardColors.RED => Color.Red
            case cardColors.BLUE => Color.Blue
            case cardColors.GREEN => Color.Green
            case cardColors.YELLOW => Color.Yellow
            case _ => Color.Black
          }
        }
      case None =>
        println("No chosen color, setting default label.")
        new scalafx.scene.control.Label(s"Current top card: ${controller.field.topCard.getValue}") {
          textFill = controller.field.topCard.getColorCode
        }
    }
    controller.setChosenColor(None)

    val handBox = new scalafx.scene.layout.HBox {
      spacing = 10
      children = controller.field.players(controller.field.currentPlayer).hand.cards.map { card =>
        val imageUrl = getClass.getResource(s"/images/${card.getColor}_${card.getValue}.png")
        if (imageUrl == null) {
          println(s"Image not found for ${card.getColor}_${card.getValue}")
        }

        val cardImage = new ImageView(new Image(imageUrl.toString)) {
          fitHeight = 150
          fitWidth = 100
        }
        cardImage.onMouseClicked = _ => {
          if (card.getValue == cardValues.WILD || card.getValue == cardValues.WILD_DRAW_FOUR) {
            showColorButtons(card, pane)
          } else if (controller.field.players(controller.field.currentPlayer).valid(card) && card.canBePlayed(controller.field.topCard)) {
            controller.play(card.asInstanceOf[Card])
            Platform.runLater(() => gui.display())
          } else {
            showAlert("Invalid Move", "The selected card cannot be played.")
          }
        }
        cardImage
      }
    }

    val drawButton = new scalafx.scene.control.Button("Draw Card") {
      onAction = _ => {
        controller.draw()
        Platform.runLater(() => gui.display())
      }
    }

    val undoButton = new scalafx.scene.control.Button("Undo") {
      onAction = _ => {
        controller.undo()
        Platform.runLater(() => gui.display())
      }
    }

    val redoButton = new scalafx.scene.control.Button("Redo") {
      onAction = _ => {
        controller.redo()
        Platform.runLater(() => gui.display())
      }
    }

    val saveButton = new scalafx.scene.control.Button("Save Game") {
      onAction = _ => {
      controller.saveGame()
      gui.setState(new BeginState(gui, controller))
      //gui.display()
      gui.getMenuPane.toFront()
        gui.display()
      showAlert("Game Saved", "The game state has been saved.")
    }
  }

    val quitButton = new scalafx.scene.control.Button("Quit") {
      onAction = _ => {
        controller.notifyObservers(Event.Quit)
        Platform.exit()
      }
    }

    val buttonLayout = new scalafx.scene.layout.HBox {
      spacing = 10
      children = Seq(drawButton, undoButton, redoButton, saveButton, quitButton)
    }

    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(playerLabel, topCardLabel, handBox, buttonLayout)
    }
    pane.children.add(layout)
  }

  private def showColorButtons(card: ICard, pane: Pane): Unit = {
    pane.children.clear() // Clear current UI elements to show color options
    val label = new scalafx.scene.control.Label("Choose a color for the wild card:")

    val redButton = new scalafx.scene.control.Button("Red") {
      onAction = _ => chooseColor(card, cardColors.RED)
    }
    val blueButton = new scalafx.scene.control.Button("Blue") {
      onAction = _ => chooseColor(card, cardColors.BLUE)
    }
    val greenButton = new scalafx.scene.control.Button("Green") {
      onAction = _ => chooseColor(card, cardColors.GREEN)
    }
    val yellowButton = new scalafx.scene.control.Button("Yellow") {
      onAction = _ => chooseColor(card, cardColors.YELLOW)
    }

    val buttonLayout = new scalafx.scene.layout.HBox {
      spacing = 10
      children = Seq(redButton, blueButton, greenButton, yellowButton)
    }

    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(label, buttonLayout)
    }

    pane.children.add(layout)
  }

  private def chooseColor(card: ICard, color: model.cardComponent.cardColors): Unit = {
    controller.setChosenColor(Some(color))
    controller.play(card.asInstanceOf[Card].copy(color = color))
    Platform.runLater(() => gui.display())
  }

  private def showAlert(alertTitle: String, message: String): Unit = {
    new scalafx.scene.control.Alert(scalafx.scene.control.Alert.AlertType.Information) {
      initOwner(gui.stage)
      this.title = alertTitle
      headerText = None
      contentText = message
    }.showAndWait()
  }
}

class UnoGUI(controller: IUnoController) extends JFXApp3 with Observer {
  controller.addObserver(this)

  private var state: State = new BeginState(this, controller)
  private val menuPane: Pane = new Pane
  private val gamePane: Pane = new Pane
  private val rootPane: StackPane = new StackPane {
    children = List(menuPane, gamePane)
  }

  def setState(newState: State): Unit = {
    state = newState
  }

  def getMenuPane: Pane = menuPane

  def display(): Unit = {
    state.display(gamePane)
    gamePane.toFront()
  }

  override def start(): Unit = {
    controller.setGuiActive(true) // new
    stage = new PrimaryStage {
      title = "Uno Game"
      width = 800
      height = 600
      scene = new Scene {
        fill = Color.Pink
        content = rootPane
      }
    }
    stage.show()
    stage.scene().onKeyPressed = handleKeyPress
    display()
  }

  private def handleKeyPress: KeyEvent => Unit = {
    case ke: KeyEvent if ke.code == KeyCode.Left =>
      state = new BeginState(this, controller)
      display()
    case _ =>
  }

  override def update(e: Event): Unit = {
    println(s"GUI Received event: $e")
    e match {
      case Event.Start =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Event.Play | Event.Draw | Event.Undo | Event.Redo | Event.Error =>
        Platform.runLater(() => {
          state.display(gamePane) // Refresh the game state
        })
      case Event.Quit =>
        Platform.exit()
    }
  }
}

object UnoGUI {
  def launchApp(controller: IUnoController): Unit = {
    new UnoGUI(controller).main(Array.empty)
  }
}