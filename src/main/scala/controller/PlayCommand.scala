package controller
import scala.controller.*
import scala.model.*
import scala.util.*
import scala.view.*
class PlayCommand(controller: UnoController, card: Card) extends util.Command {
  private var previousState: Option[UnoField] = None

  override def execute(): Try[Unit] = Try {
    previousState = Some(controller.field) // Save state for undo
    controller.play(card)
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
