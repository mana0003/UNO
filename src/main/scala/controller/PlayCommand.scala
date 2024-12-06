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
    previousState = Some(controller.field) // Save state for undo
    //controller.play(card)
    val currentPlayer = controller.field.players(controller.field.currentPlayer)

    // Validate the move 
    if (!card.canBePlayedOn(controller.field.topCard)) {
      throw new IllegalArgumentException("Card cannot be played on the current top card")
    }

    // Update players hand by removing card
    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))

    // Update the list of players
    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    // Update the game state with the new topCard and move to the next player
    controller.field = controller.field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = (controller.field.currentPlayer + 1) % controller.field.players.size
    )

    // Notify observers about the play event
    controller.notifyObservers(Event.Play)
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
