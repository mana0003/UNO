package model.gameComponent.gameIm

import model.cardComponent.cardIm.Card

import scala.util.{Failure, Success, Try}

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
