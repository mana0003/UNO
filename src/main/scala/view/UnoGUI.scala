package view

import controller.controllerComponent.IUnoController
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.{Scene, Node}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{HBox, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.Includes.*
import scalafx.scene.control.{Button, Label, ScrollPane}
import util.{Event, Observer}
import util.Event.*
import model.cardComponent.{ICard, cardColors, cardValues}
import model.cardComponent.cardIm.Card
import model.gameComponent.IPlayer
import scalafx.scene.image.{Image, ImageView}
import controller.patterns.{ConcreteUnoActionProcessor, UnoActionHandler, UnoActionProcessor, UnoActionStrategy}
import controller.controllerComponent.ControllerIm.UnoController
import scalafx.util.Duration
import scalafx.animation.TranslateTransition
import controller.patterns.UnoActionBuilder.*

trait State {
  def display(pane: Pane): Unit
}

class BeginState(gui: UnoGUI, controller: IUnoController) extends State {
  override def display(pane: Pane): Unit = {
    pane.children.clear()
    val currentPlayer = controller.getField.players(controller.getCurrentPlayer)

    val logoImage = new ImageView(new Image(getClass.getResource("/extras/Logo.png").toString)) {
      fitHeight = 400
      fitWidth = 500
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

//var cPlayer = controller.field.currentPlayer
// Update the GameState class to use images for the cards
def slideDown(targetNode: Node, onFinish: => Unit): Unit = {
  val transition = new TranslateTransition {
    duration = Duration(500)
    byY = 600
    node = targetNode
    onFinished = _ => onFinish
  }
  transition.play()
}

class GameState(gui: UnoGUI, controller: IUnoController) extends State {
  private val cardThreshold = 7

  override def display(pane: Pane): Unit = {
    println("Inside GameState display")
    pane.children.clear()

    val playerLabel = new Label(s"Current player: Player ${controller.field.currentPlayer + 1}")
    val topCardLabel = controller.getChosenColor match {
      case Some(color) =>
        println(s"Chosen color: $color")
        new Label(s"Your opponent chose the color: $color") {
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
        new Label(s"Current top card:") {}
        new ImageView(new Image(getClass.getResource(s"/images/${controller.field.topCard.getColor}_${controller.field.topCard.getValue}.png").toString)) {
          fitHeight = 150
          fitWidth = 100
        }
    }
    controller.setChosenColor(None)

    val handBox = new HBox {
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
            /*if (controller.getChosenColor.isDefined) {
              controller.play(card.asInstanceOf[Card].copy(color = controller.getChosenColor.get))
              Platform.runLater(() => gui.display())
            }*/
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

    val handContainer = if (controller.field.players(controller.field.currentPlayer).hand.cards.size > cardThreshold) {
      new ScrollPane {
        content = handBox
        fitToHeight = false
        fitToWidth = true
        prefHeight = 200
        prefWidth = 800
      }
    } else {
      handBox
    }

    val drawButton = new Button("Draw Card") {
      onAction = _ => {
        controller.draw()
        Platform.runLater(() => gui.display())
      }
    }

    val undoButton = new Button("Undo") {
      onAction = _ => {
        controller.undo()
        Platform.runLater(() => gui.display())
      }
    }

    val redoButton = new Button("Redo") {
      onAction = _ => {
        controller.redo()
        Platform.runLater(() => gui.display())
      }
    }

    val saveButton = new Button("Save Game") {
      onAction = _ => {
        controller.saveGame()
        gui.setState(new BeginState(gui, controller))
        gui.getMenuPane.toFront()
        gui.display()
        showAlert("Game Saved", "The game state has been saved.")
      }
    }

    val quitButton = new Button("Quit") {
      onAction = _ => {
        controller.notifyObservers(Event.Quit)
        Platform.exit()
      }
    }

    val buttonLayout = new HBox {
      spacing = 10
      children = Seq(drawButton, undoButton, redoButton, saveButton, quitButton)
    }

    val layout = new VBox {
      spacing = 20
      children = Seq(playerLabel, topCardLabel, handContainer, buttonLayout)
    }
    pane.children.add(layout)
  }

  private def showColorButtons(card: ICard, pane: Pane): Unit = {
    pane.children.clear() // Clear current UI elements to show color options
    val label = new Label("Choose a color for the wild card:")

    val redButton = new Button("Red") {
      onAction = _ => chooseColor(card, cardColors.RED)
    }
    val blueButton = new Button("Blue") {
      onAction = _ => chooseColor(card, cardColors.BLUE)
    }
    val greenButton = new Button("Green") {
      onAction = _ => chooseColor(card, cardColors.GREEN)
    }
    val yellowButton = new Button("Yellow") {
      onAction = _ => chooseColor(card, cardColors.YELLOW)
    }

    val buttonLayout = new HBox {
      spacing = 10
      children = Seq(redButton, blueButton, greenButton, yellowButton)
    }

    val layout = new VBox {
      spacing = 20
      children = Seq(label, buttonLayout)
    }

    pane.children.add(layout)
  }

  private def chooseColor(card: ICard, color: cardColors): Unit = {
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
        fill = Color.DarkRed
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
      case Event.Play | Event.Draw =>
        var currentPlayer = (controller.getField.currentPlayer + 1) % controller.getField.players.length
        Platform.runLater(() => {
          state.display(gamePane) // Refresh the game state
        })
      case Event.Undo | Event.Redo | Event.Error =>
        Platform.runLater(() => {
          state.display(gamePane) // Refresh the game state
        })
      case Event.Quit =>
        Platform.exit()
      case Event.Win =>
        Platform.runLater(() => {
          new scalafx.scene.control.Alert(scalafx.scene.control.Alert.AlertType.Information) {
            initOwner(stage)
            title = "Game Over"
            headerText = None
            contentText = s"Player ${controller.getField.currentPlayer} has won the game!"
          }.showAndWait()
          controller.notifyObservers(Event.Quit)
        })
    }
  }

  def processPlayerAction(player: IPlayer, action: String): Unit = {
    val actionBuilder = new UnoActionBuilder()
    val actionProcessor = new ConcreteUnoActionProcessor(actionBuilder)
    actionProcessor.processAction(controller.asInstanceOf[UnoController], player, action)
  }

  def executeStrategy(player: IPlayer, strategy: UnoActionStrategy): Unit = {
    val actionHandler = new UnoActionHandler(strategy)
    actionHandler.executeStrategy(controller, player)
  }
}

object UnoGUI {
  def launchApp(controller: IUnoController): Unit = {
    new UnoGUI(controller).main(Array.empty)
  }
}