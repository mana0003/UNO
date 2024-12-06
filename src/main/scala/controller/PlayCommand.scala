package controller
import controller.*
import model.*
import util.*
import view.*
import scala.util.Failure
import scala.util.Try


class PlayCommand(controller: UnoController, card: Card) extends util.Command {
  private var previousState: Option[UnoField] = None

  override def execute(): Try[Unit] = Try {
    previousState = Some(controller.field) // Save the current game state

    val currentPlayer = controller.field.players(controller.field.currentPlayer)

    if (!card.canBePlayedOn(controller.field.topCard)) {
      throw new IllegalArgumentException("Card cannot be played on the current top card")
    }

    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    val nextPlayer = (controller.field.currentPlayer + 1) % controller.field.players.length
    controller.field = controller.field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = nextPlayer
    )
    controller.notifyObservers(Event.Play)
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
    val currentPlayer = controller.field.players(controller.field.currentPlayer)

    if (!card.canBePlayedOn(controller.field.topCard)) {
      throw new IllegalStateException("Card cannot be redone on the current top card")
    }

    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    val nextPlayer = (controller.field.currentPlayer + 1) % controller.field.players.length
    controller.field = controller.field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = nextPlayer
    )
    controller.notifyObservers(Event.Redo)
  }
}
