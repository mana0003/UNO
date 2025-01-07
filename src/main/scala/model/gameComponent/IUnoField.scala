package model.gameComponent

import model.cardComponent.cardIm.*

trait IUnoField {
  def players: List[Player]
  def topCard: Card
  def currentPlayer: Int
}