package model.gameComponent

import model.cardComponent.*

trait IPlayerHand {
  def cards: List[Card]
  def addCard(card: Card): IPlayerHand
  def removeCard(card: Card): IPlayerHand
}