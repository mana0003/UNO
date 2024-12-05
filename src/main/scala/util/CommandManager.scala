package util

import scala.util.{Try, Success, Failure}

class CommandManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(command: Command): Try[Unit] = {
    command.execute() match {
      case Success(_) =>
        undoStack = command :: undoStack
        redoStack = Nil
        Success(())
      case Failure(exception) => Failure(exception)
    }
  }

  def undoStep: Try[Unit] = {
    undoStack match {
      case Nil => Failure(new NoSuchElementException("No commands to undo"))
      case head :: stack =>
        head.undo() match {
          case Success(_) =>
            undoStack = stack
            redoStack = head :: redoStack
            Success(())
          case Failure(exception) => Failure(exception)
        }
    }
  }

  def redoStep: Try[Unit] = {
    redoStack match {
      case Nil => Failure(new NoSuchElementException("No commands to redo"))
      case head :: stack =>
        head.execute() match {
          case Success(_) =>
            redoStack = stack
            undoStack = head :: undoStack
            Success(())
          case Failure(exception) => Failure(exception)
        }
    }
  }
}