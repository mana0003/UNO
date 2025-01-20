package controller.controllerComponent.ControllerIm
import UNO.MainModule
import controller.*
import util.{Event, Observer}
import controller.controllerComponent.IUnoController
import com.google.inject.{Guice, Inject, Injector}
import model.cardComponent.cardColors
import model.gameComponent.IUnoField
import model.cardComponent.ICard
import controller.command.commandIm.{DrawCommand, PlayCommand}
import util.*
import model.*
import model.fileIoComponent.IFileIo
import scala.io.AnsiColor.*
import scala.util.{Failure, Success}

class UnoController @Inject() (var field: IUnoField, fileIO: IFileIo) extends IUnoController with Observable {
  private val commandManager = new CommandManager()
  private var isGuiActive: Boolean = false
  private var chosenColor: Option[cardColors] = None
  val injector: Injector = Guice.createInjector(new MainModule)

  def setGuiActive(active: Boolean): Unit = {
    isGuiActive = active
  }

  def isGuiMode: Boolean = isGuiActive

  override def play(card: ICard): Unit = {
    val command = new PlayCommand(this, card)
    commandManager.doStep(command) match {
      case Success(_) =>
        println(s"Player ${field.currentPlayer + 1} played: ${card.getColorCode}${card.getValue}$RESET")
        println(s"Player ${field.currentPlayer + 1} hand: ${field.players(field.currentPlayer).hand.cards.map(c => s"${c.getColorCode}${c.getValue}$RESET").mkString(", ")}")
        notifyObservers(Event.Play)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  override def draw(): Unit = {
    val command = new DrawCommand(this)
    commandManager.doStep(command) match {
      case Success(_) =>
        println(s"Player ${field.currentPlayer + 1} drew a card.")
        notifyObservers(Event.Draw)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  override def undo(): Unit = {
    commandManager.undoStep() match {
      case Success(_) =>
        notifyObservers(Event.Undo)
      case Failure(_) =>
        notifyObservers(Event.Error)
    }
  }

  override def redo(): Unit = {
    commandManager.redoStep() match {
      case Success(_) =>
        notifyObservers(Event.Redo)
      case Failure(_) =>
        notifyObservers(Event.Error)
    }
  }

  override def startGame(): Unit = {
    notifyObservers(Event.Start)
  }

  override def getField: IUnoField = field

  override def getCurrentPlayer: Int = field.currentPlayer

  override def addObserver(observer: Observer): Unit = {
    super[Observable].addObserver(observer)
  }

  override def notifyObservers(event: Event): Unit = {
    super[Observable].notifyObservers(event)
  }

  override def getChosenColor: Option[cardColors] = chosenColor

  override def setChosenColor(color: Option[cardColors]): Unit = {
    chosenColor = color
  }

  override def saveGame(): Unit = {
    fileIO.save(field)
    notifyObservers(Event.Start)
  }

  override def loadGame(): Unit = {
    field = fileIO.load
    notifyObservers(Event.Play)
  }
}