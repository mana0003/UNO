package controller
import model.*
import util.*
import view.*
import controller.*
import scala.util.Try
import scala.util.Failure


class DrawCommand(controller: UnoController) extends util.Command {
  private var previousState: Option[UnoField] = None
  private var drawnCard: Option[Card] = None

  override def execute(): Try[Unit] = Try {
    previousState = Some(controller.field) // Save the current game state
    val newCard = randomCard // Draw a random card
    drawnCard = Some(newCard) // Save the drawn card for redo

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.addCard(newCard))

    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    controller.field = controller.field.copy(players = updatedPlayers)
    controller.notifyObservers(Event.Draw)
  }

  override def undo(): Try[Unit] = previousState match {
    case Some(state) =>
      Try {
        controller.field = state // Revert to the previous state
        controller.notifyObservers(Event.Undo)
      }
    case None => Failure(new IllegalStateException("No state to undo to"))
  }

  override def redo(): Try[Unit] = Try {
    if (drawnCard.isEmpty) throw new IllegalStateException("No card to redo drawing")
    val newCard = drawnCard.get

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.addCard(newCard))

    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    controller.field = controller.field.copy(players = updatedPlayers)
    controller.notifyObservers(Event.Redo)
  }
}
