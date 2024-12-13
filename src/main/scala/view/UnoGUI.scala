package view

import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.Includes._
import controller.UnoController
import util.{Event, Observer}
import util.Event._

trait State {
  def display(pane: Pane): Unit
}

class BeginState(gui: UnoGUI, controller: UnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()
    val beginLabel = new scalafx.scene.control.Label("Welcome to UNO Game")
    val startButton = new scalafx.scene.control.Button("Start Game") {
      onAction = _ => {
        gui.setState(new GameState(gui, controller))
        gui.display()
      }
    }
    pane.children.addAll(beginLabel, startButton)
  }
}

class GameState(gui: UnoGUI, controller: UnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()
    val playerLabel = new scalafx.scene.control.Label(s"Current player: Player ${controller.field.currentPlayer + 1}")
    val topCardLabel = new scalafx.scene.control.Label(s"Current top card: ${controller.field.topCard.getColorCode}${controller.field.topCard.value}")
    val handListView = new scalafx.scene.control.ListView[String] {
      items = scalafx.collections.ObservableBuffer(controller.field.players(controller.field.currentPlayer).hand.cards.map(card => s"${card.getColorCode}${card.value}"): _*)
    }
    val drawButton = new scalafx.scene.control.Button("Draw Card") {
      onAction = _ => controller.draw()
    }
    val playButton = new scalafx.scene.control.Button("Play Card") {
      onAction = _ => {
        val selectedCardIndex = handListView.selectionModel().getSelectedIndex
        if (selectedCardIndex >= 0) {
          val card = controller.field.players(controller.field.currentPlayer).hand.cards(selectedCardIndex)
          if (controller.field.players(controller.field.currentPlayer).valid(card) && card.canBePlayedOn(controller.field.topCard)) {
            controller.play(card)
          } else {
            showAlert("Invalid Move", "The selected card cannot be played.")
          }
        } else {
          showAlert("No Card Selected", "Please select a card to play.")
        }
      }
    }
    pane.children.addAll(playerLabel, topCardLabel, handListView, drawButton, playButton)
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

class UnoGUI(controller: UnoController) extends JFXApp3 with Observer {
  controller.add(this)

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
    stage = new PrimaryStage {
      title = "Uno Game"
      scene = new Scene {
        fill = Color.Black
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
      case Start =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Play =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Draw =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Undo =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Redo =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Error =>
        Platform.runLater(() => {
          state = new GameState(this, controller)
          display()
        })
      case Quit =>
        Platform.exit()
    }
  }
}

object UnoGUI {
  def launchApp(controller: UnoController): Unit = {
    new UnoGUI(controller).main(Array.empty)
  }
}