package controller.command.commandIm

import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import model.*
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.UnoField
import util.*
//import view.*
import scala.util.Failure
import scala.util.Try
import model.randomCards
import scala.util.{Failure, Try}

class PlayCommand(controller: UnoController, card: Card) extends util.Command {
  private var previousState: Option[UnoField] = None

  override def doStep(command: Command): Try[Unit] = Try {
    previousState = Some(controller.field) // Save the current game state

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val nextPlayerIndex = (controller.field.currentPlayer + 1) % controller.field.players.length
    val nextPlayer = controller.field.players(nextPlayerIndex)

    if (!card.canBePlayedOn(controller.field.topCard)) {
      throw new IllegalArgumentException("Card cannot be played on the current top card")
    }

    val updatedCurrentPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedPlayers = controller.field.players.updated(controller.field.currentPlayer, updatedCurrentPlayer)

    val cardsToDraw = card.value match {
      case cardValues.DRAW_TWO        => 2
      case cardValues.WILD_DRAW_FOUR  => 4
      case _                          => 0
    }

    val finalPlayers = if (cardsToDraw > 0) {
      val drawnCards = randomCards(cardsToDraw)  // Draw random cards directly
      val updatedNextPlayerHand = drawnCards.foldLeft(nextPlayer.hand)((hand, newCard) => hand.addCard(newCard))
      updatedPlayers.updated(nextPlayerIndex, nextPlayer.copy(hand = updatedNextPlayerHand))
    } else {
      updatedPlayers
    }

    /*val chosenColor = controller.getChosenColorFromGUI() match {
      case Some(color) => color // If GUI-based color selection
      case None => controller.getChosenColor() // If console-based color selection
    }*/

    val newCurrentPlayer = if (card.value == cardValues.SKIP) (controller.field.currentPlayer + 2) % controller.field.players.length else nextPlayerIndex

    // Handle Wild and Wild Draw Four cards with color selection
    val updatedTopCard = card.value match {
      case cardValues.WILD | cardValues.WILD_DRAW_FOUR =>
        val chosenColor = controller.getChosenColor.getOrElse(cardColors.RED) // Prompt for color selection
        card.copy(color = chosenColor)
      case _ => card
    }

    // Update game field
    controller.field = controller.field.copy(
      players = finalPlayers,
      topCard = card,
      currentPlayer = newCurrentPlayer % controller.field.players.length
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

    if (!card.canBePlayedOn(controller.field.topCard)) {
      throw new IllegalStateException("Card cannot be redone on the current top card")
    }

    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.removeCard(card))
    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    )

    val isSkipCard = card.value match {
      case cardValues.SKIP => true
      case _ => false
    }
    val nextPlayer = if (isSkipCard) controller.field.currentPlayer else (controller.field.currentPlayer + 1) % controller.field.players.length

    //val nextPlayer = (controller.field.currentPlayer + 1) % controller.field.players.length
    controller.field = controller.field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = nextPlayer
    )
    controller.notifyObservers(Event.Redo)
  }
}