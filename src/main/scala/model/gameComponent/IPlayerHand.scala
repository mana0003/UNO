// File: src/main/scala/model/gameComponent/IPlayerHand.scala
package model.gameComponent

import model.cardComponent.ICard

trait IPlayerHand {
  def cards: List[ICard]
  def addCard(card: ICard): IPlayerHand
  def removeCard(card: ICard): IPlayerHand
  def toXml: scala.xml.Node
}