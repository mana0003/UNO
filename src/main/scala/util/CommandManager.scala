package util

import scala.util.{Try, Success, Failure}
import util.Command

class CommandManager extends Command {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  override def doStep(command: Command): Try[Unit] = {
    val result = command.doStep(command)
    result match {
      case Success(_) =>
        undoStack = undoStack
        redoStack = Nil // Clear the redo stack after a new command
      case Failure(_) => // Do nothing
    }
    result
  }

  override def undoStep(): Try[Unit] = {
    undoStack match {
      case Nil => Failure(new NoSuchElementException("No commands to undo"))
      case head :: tail =>
        val result = head.undoStep()
        result match {
          case Success(_) =>
            undoStack = tail
            redoStack = head :: redoStack
          case Failure(_) => // Do nothing
        }
        result
    }
  }

  override def redoStep(): Try[Unit] = {
    redoStack match {
      case Nil => Failure(new NoSuchElementException("No commands to redo"))
      case head :: tail =>
        val result = head.redoStep()
        result match {
          case Success(_) =>
            redoStack = tail
            undoStack = head :: undoStack
          case Failure(_) => // Do nothing
        }
        result
    }
  }
}