package controller.command.commandIm

import controller.*
import controller.controllerComponent.IUnoController
import model.gameComponent.{IUnoField, IPlayer, IPlayerHand}
import util.*
import model.cardComponent.{ICard, cardColors, cardValues}
import model.cardComponent.cardIm.{randomCards, Card}
import scala.util.{Failure, Try}

class PlayCommand(controller: IUnoController, card: ICard) extends util.Command {
  private var previousState: Option[IUnoField] = None

  override def doStep(command: Command): Try[Unit] = Try {
    previousState = Some(controller.field) // Save the current game state

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val nextPlayerIndex = (controller.field.currentPlayer + 1) % controller.field.players.length
    val nextPlayer = controller.field.players(nextPlayerIndex)

    if (!card.canBePlayed(controller.field.topCard)) {
      throw new IllegalArgumentException("Card cannot be played on the current top card")
    }
    //val updatedCurrentPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedCurrentPlayer = currentPlayer.copy(
      hand = currentPlayer.hand.removeCard(card)
    )
    val updatedPlayers = controller.field.players.updated(controller.field.currentPlayer, updatedCurrentPlayer)

    val cardsToDraw = card.getValue match {
      case cardValues.DRAW_TWO => 2
      case cardValues.WILD_DRAW_FOUR => 4
      case _ => 0
    }

    // Update game field
    val finalPlayers = if (cardsToDraw > 0) {
      val drawnCards = randomCards(cardsToDraw) // Draw random cards directly
      val updatedNextPlayerHand = drawnCards.foldLeft(nextPlayer.hand)((hand, newCard) => hand.addCard(newCard)) // next Player
      updatedPlayers.updated(nextPlayerIndex, nextPlayer.copy(hand = updatedNextPlayerHand)) // next Player
      // remove the card from the current player
    } else {
      //updatedPlayers
      controller.field.players.updated(controller.field.currentPlayer, updatedCurrentPlayer)
    }

    val newCurrentPlayer = if (card.getValue == cardValues.SKIP) (controller.field.currentPlayer + 2) % controller.field.players.length else nextPlayerIndex

    // Handle Wild and Wild Draw Four cards with color selection
    val updatedTopCard = card.getValue match {
      case cardValues.WILD | cardValues.WILD_DRAW_FOUR =>
        controller.getChosenColor match {
          case Some(chosenColor) =>
            controller.setChosenColor(None)
            card.asInstanceOf[Card].copy(color = chosenColor)
          case None =>
            throw new IllegalStateException("No color chosen for Wild or Wild Draw Four card")
        }
      case _ => card
    }

    controller.field = controller.field.copy(
      players = finalPlayers,
      topCard = updatedTopCard,
      currentPlayer = if (card.getValue == cardValues.SKIP)
                        (controller.field.currentPlayer + 2) % controller.field.players.length
                      else
                        (controller.field.currentPlayer + 1) % controller.field.players.length
    )
    controller.notifyObservers(Event.Play)
  }

  override def undoStep(): Try[Unit] = previousState match {
    case Some(state) =>
      Try {
        controller.field = state // Revert to the previous state
        controller.notifyObservers(Event.Undo)
      }
    case None => Failure(new IllegalStateException("No state to undo to"))
  }

  override def redoStep(): Try[Unit] = Try {
    previousState = Some(controller.field)

    val currentPlayer = controller.field.players(controller.field.currentPlayer)

    if (!card.canBePlayed(controller.field.topCard)) {
      throw new IllegalStateException("Card cannot be redone on the current top card")
    }

    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedPlayers = controller.field.players.updated(controller.field.currentPlayer, updatedPlayer)

    val isSkipCard = card.getValue == cardValues.SKIP
    val nextPlayer = if (isSkipCard) controller.field.currentPlayer else (controller.field.currentPlayer + 1) % controller.field.players.length

    controller.field = controller.field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = nextPlayer
    )
    controller.notifyObservers(Event.Redo)
  }
}