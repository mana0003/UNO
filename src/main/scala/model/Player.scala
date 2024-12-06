package model
import controller.*
import view.*
import model.*
import util.*
import scala.util.Failure
import scala.util.Success
import scala.util.Try

case class Player(id: Int, hand: PlayerHand) {
  def valid(card: Card): Boolean = {
    hand.cards.exists(_.canBePlayedOn(card))
  }

  def play(card: Card): Try[Player] = {
    if (valid(card)) {
      Success(Player(id, hand.removeCard(card)))
    } else {
      Failure(IllegalArgumentException("Illeagal move."))
    }
  }
}