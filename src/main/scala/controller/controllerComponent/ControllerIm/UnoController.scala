package controller.controllerComponent.ControllerIm

import controller.*
import controller.controllerComponent.IUnoController
import model.*
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.UnoField
import util.*

import scala.io.AnsiColor.*
import scala.util.{Failure, Success}

class UnoController(var field: UnoField) extends IUnoController with Observable {
  private val commandManager = new CommandManager()
  private var isGuiActive: Boolean = false
  private var chosenColor: Option[cardColors] = None

  def setGuiActive(active: Boolean): Unit = {
    isGuiActive = active
  }

  def isGuiMode: Boolean = isGuiActive


  def play(card: Card): Unit = {
    val command = new PlayCommand(this, card)
    commandManager.doStep(command) match {
      case Success(_) =>
        println(s"Player ${field.currentPlayer + 1} played: ${card.getColorCode}${card.value}$RESET")
        println(s"Player ${field.currentPlayer + 1} hand: ${field.players(field.currentPlayer).hand.cards.map(c => s"${c.getColorCode}${c.value}$RESET").mkString(", ")}")
        notifyObservers(Event.Play)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  def draw(): Unit = {
    val command = new DrawCommand(this)
    commandManager.doStep(command) match {
      case Success(_) =>
        notifyObservers(Event.Draw)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  def undo(): Unit = {
    commandManager.undoStep() match {
      case Success(_) =>
        notifyObservers(Event.Undo)
      case Failure(_) =>
        notifyObservers(Event.Error)
    }
  }

  def redo(): Unit = {
    commandManager.redoStep() match {
      case Success(_) =>
        notifyObservers(Event.Redo)
      case Failure(_) =>
        notifyObservers(Event.Error)
    }
  }

  def startGame(): Unit = {
    notifyObservers(Event.Start)
  }

  def getField: UnoField = field

  def getCurrentPlayer: Int = field.currentPlayer

  override def addObserver(observer: Observer): Unit = {
    super.addObserver(observer)
  }
    
  override def notifyObservers(event: Event): Unit = {
    super.notifyObservers(event)
  }

  def getChosenColor: Option[cardColors] = chosenColor

  def setChosenColor(color: Option[cardColors]): Unit = {
    chosenColor = color
  }
}