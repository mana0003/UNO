package model.gameComponent

import model.cardComponent.ICard

trait IPlayerHand {
  def cards: List[ICard]
  def addCard(card: ICard): IPlayerHand
  def removeCard(card: ICard): IPlayerHand
}