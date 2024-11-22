package scala.controller
import scala.model.*
import scala.view.*
import scala.util.*

class UnoController(var field: UnoField) extends Observable {

  def play(card: Card): Unit = {
    val updatedPlayer = field.players(field.currentPlayer).play(card).get
    val updatedPlayers = field.players.updated(field.currentPlayer, updatedPlayer)
    if (updatedPlayer.hand.cards.isEmpty) {
      notifyObservers(Event.Quit)
      return
    }
    field = field.copy(
      players = updatedPlayers,
      topCard = card,
      currentPlayer = (field.currentPlayer + 1) % field.players.length
    )
    notifyObservers(Event.Play)
  }
  def draw(): Unit = {
    val newCard = randomCard
    val updatedPlayer = Player(
      field.currentPlayer,
      field.players(field.currentPlayer).hand.addCard(newCard)
    )
    val updatedPlayers = field.players.updated(field.currentPlayer, updatedPlayer)
    field = field.copy(players = updatedPlayers)
    notifyObservers(Event.Draw)
  }
}