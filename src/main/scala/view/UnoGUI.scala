package view

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

trait State {
  def display(pane: Pane): Unit
}

class BeginState(gui: UnoGUI, controller: IUnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()
    val beginLabel = new scalafx.scene.control.Label("Welcome to UNO Game")
    val startButton = new scalafx.scene.control.Button("Start Game") {
      onAction = _ => {
        gui.setState(new GameState(gui, controller))
        gui.display()
      }
    }
    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(beginLabel, startButton)
    }
    pane.children.add(layout)
  }
}

class GameState(gui: UnoGUI, controller: IUnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()

    val playerLabel = new scalafx.scene.control.Label(s"Current player: Player ${controller.field.currentPlayer + 1}")
    /*val topCardLabel = new scalafx.scene.control.Label(s"Current top card: ${controller.field.topCard.value}") {
      textFill = controller.field.topCard.getColorCode
    }*/
    val topCardLabel = controller.getChosenColor match {
      case Some(color) =>
        new scalafx.scene.control.Label(s"Your opponent chose the color: $color") {
          textFill = color match {
            case cardColors.RED => Color.Red
            case cardColors.BLUE => Color.Blue
            case cardColors.GREEN => Color.Green
            case cardColors.YELLOW => Color.Yellow
          }
        }
      case None =>
        new scalafx.scene.control.Label(s"Current top card: ${controller.field.topCard.value}") {
          textFill = controller.field.topCard.getColorCode
        }
    }
    controller.setChosenColor(None)

    val handListView = new ListView[ICard] {
      items = scalafx.collections.ObservableBuffer(controller.field.players(controller.field.currentPlayer).hand.cards: _*)

      cellFactory = new javafx.util.Callback[javafx.scene.control.ListView[ICard], javafx.scene.control.ListCell[ICard]] {
  override def call(param: javafx.scene.control.ListView[ICard]): javafx.scene.control.ListCell[ICard] = {
    new ListCell[ICard] {
      item.onChange { (_, _, cardOpt) =>
        val cardOption = Option(cardOpt)
        text = cardOption.map(_.value.toString).getOrElse("")
        textFill = cardOption.map(_.getColorCode).getOrElse(Color.Pink)
      }
    }
  }
}
    }

    val drawButton = new scalafx.scene.control.Button("Draw Card") {
      onAction = _ => {
        controller.draw()
        Platform.runLater(() => {
          gui.display()
        })
      }
    }

    val playButton = new scalafx.scene.control.Button("Play Card") {
      onAction = _ => {
        val selectedCardIndex = handListView.selectionModel().getSelectedIndex
        if (selectedCardIndex >= 0) {
          val card = controller.field.players(controller.field.currentPlayer).hand.cards(selectedCardIndex)
          if (card.value == cardValues.WILD || card.value == cardValues.WILD_DRAW_FOUR) {
            showColorButtons(card, pane)
          } else if (controller.field.players(controller.field.currentPlayer).valid(card) && card.canBePlayed(controller.field.topCard)) {
            controller.play(card)
            Platform.runLater(() => {
              gui.display()
            })
          } else {
            showAlert("Invalid Move", "The selected card cannot be played.")
          }
        } else {
          showAlert("No Card Selected", "Please select a card to play.")
        }
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

    val quitButton = new scalafx.scene.control.Button("Quit") {
      onAction = _ => {
        controller.notifyObservers(Event.Quit)
        Platform.exit()
      }
    }

    val buttonLayout = new scalafx.scene.layout.HBox {
      spacing = 10
      children = Seq(drawButton, playButton, undoButton, redoButton, quitButton)
    }

    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(playerLabel, topCardLabel, handListView, buttonLayout)
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
    controller.play(card.copy(color = Some(color)))
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