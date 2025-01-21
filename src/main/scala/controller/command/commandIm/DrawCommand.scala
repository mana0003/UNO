package controller.command.commandIm

//import controller.command.IDrawCommand

import controller.controllerComponent.IUnoController

import model.cardComponent.cardIm.*
import model.cardComponent.ICard
import model.gameComponent.IUnoField
import model.gameComponent.IPlayer
import util.*
//import view.*
import controller.*
import model.*
import scala.util.{Failure, Try}


class DrawCommand(controller: IUnoController) extends util.Command {
  var previousState: Option[IUnoField] = None
  var drawnCard: Option[ICard] = None

  override def doStep(command: Command): Try[Unit] = Try {
    previousState = Some(controller.field) // Save the current game state
    val newCard = randomCard // Draw a random card
    drawnCard = Some(newCard) // Save the drawn card for redo

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.addCard(newCard))

    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    ).collect { case player: IPlayer => player }
    val topCard: ICard = controller.field.topCard
    //val currentPlayer: Int = controller.field.currentPlayer

    controller.field = controller.field.copy(players = updatedPlayers, topCard = topCard, currentPlayer = (controller.field.currentPlayer + 1) % controller.field.players.length)
    controller.notifyObservers(Event.Draw)
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
    if (drawnCard.isEmpty) throw new IllegalStateException("No card to redo drawing")
    val newCard = drawnCard.get

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    val updatedPlayer = currentPlayer.copy(hand = currentPlayer.hand.addCard(newCard))
    val updatedPlayers = controller.field.players.updated(
      controller.field.currentPlayer,
      updatedPlayer
    ).collect { case player: IPlayer => player }
    val topCard: ICard = controller.field.topCard
    //val currentPlayer: Int = controller.field.currentPlayer

    controller.field = controller.field.copy(players = updatedPlayers, topCard = topCard, currentPlayer = controller.field.currentPlayer)
    controller.notifyObservers(Event.Redo)
  }
}