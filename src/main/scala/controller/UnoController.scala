package controller

import model.*
import view.*
import util.*
import controller.*
import util.CommandManager
import scala.util.Success
import scala.util.Failure


class UnoController(var field: UnoField) extends Observable {
  private val commandManager = new CommandManager()

  def play(card: Card): Unit = {
    val command = new PlayCommand(this, card)
    commandManager.doStep(command) match {
      case Success(_) => // Command executed successfully
        notifyObservers(Event.Play)
      case Failure(exception) => notifyObservers(Event.Error)
        notifyObservers(Event.Error)
    }
  }

  def draw(): Unit = {
    val command = new DrawCommand(this)
    commandManager.doStep(command) match {
      case Success(_) => // Command executed successfully
        notifyObservers(Event.Draw)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  def undo(): Unit = {
    commandManager.undoStep match {
      case Success(_) =>
        notifyObservers(Event.Undo)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  def redo(): Unit = {
    commandManager.redoStep match {
      case Success(_) =>
        notifyObservers(Event.Redo)
      case Failure(exception) =>
        notifyObservers(Event.Error)
    }
  }

  def startGame(): Unit = {
    // Game-specific setup logic
    notifyObservers(Event.Start) // Notify observers that the game has started
  }
}
