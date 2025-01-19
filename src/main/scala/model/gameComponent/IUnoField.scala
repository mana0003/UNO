package model.gameComponent

import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.cardComponent.cardIm.Card

trait IUnoField {
  def players: List[IPlayer]
  def topCard: ICard
  def currentPlayer: Int
  def copy(
            players: List[IPlayer],
            topCard: ICard,
            currentPlayer: Int
          ): IUnoField
  def nextPlayer(): IUnoField
  def toXml: scala.xml.Node
}