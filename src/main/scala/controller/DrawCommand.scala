package controller
import model.*
import util.*
import view.*
import controller.*
import scala.util.Try
import scala.util.Failure

class DrawCommand(controller: UnoController) extends util.Command {
  private var previousState: Option[UnoField] = None

  override def execute(): Try[Unit] = Try {
    previousState = Some(controller.field) // Save state for undo
    controller.draw()
  }

  override def undo(): Try[Unit] = previousState match {
    case Some(state) =>
      Try {
        controller.field = state
        controller.notifyObservers(Event.Undo) // Notify that undo occurred
      }
    case None => Failure(new IllegalStateException("No state to undo to"))
  }
}
