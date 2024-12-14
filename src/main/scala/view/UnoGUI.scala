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
import model._
import scalafx.scene.control.{ListView, ListCell}

import javafx.util.Callback

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
    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(beginLabel, startButton)
    }
    pane.children.add(layout)
  }
}

class GameState(gui: UnoGUI, controller: UnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()

    val playerLabel = new scalafx.scene.control.Label(s"Current player: Player ${controller.field.currentPlayer + 1}")
    val topCardLabel = new scalafx.scene.control.Label(s"Current top card: ${controller.field.topCard.value}") {
      textFill = controller.field.topCard.getColorCode
    }

    // Update the list of cards after each action
    val handListView = new ListView[Card] {
      items = scalafx.collections.ObservableBuffer(controller.field.players(controller.field.currentPlayer).hand.cards: _*)

      cellFactory = new Callback[ListView[Card], ListCell[Card]] {
        override def call(param: ListView[Card]): ListCell[Card] = {
          new ListCell[Card] {
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
        controller.draw() // Draw a card from the deck
        // Refresh the display after drawing a card
        Platform.runLater(() => {
          gui.display() // Update the GUI after the action
        })
      }
    }

    val playButton = new scalafx.scene.control.Button("Play Card") {
      onAction = _ => {
        val selectedCardIndex = handListView.selectionModel().getSelectedIndex
        if (selectedCardIndex >= 0) {
          val card = controller.field.players(controller.field.currentPlayer).hand.cards(selectedCardIndex)
          if (controller.field.players(controller.field.currentPlayer).valid(card) && card.canBePlayedOn(controller.field.topCard)) {
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

    val layout = new scalafx.scene.layout.VBox {
      spacing = 20
      children = Seq(playerLabel, topCardLabel, handListView, drawButton, playButton)
    }
    pane.children.add(layout)
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
  def launchApp(controller: UnoController): Unit = {
    new UnoGUI(controller).main(Array.empty)
  }
}
