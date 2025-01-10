package model.gameComponent.gameIm

import com.google.inject.{AbstractModule, Guice, Inject}
import net.codingwell.scalaguice.ScalaModule
import model.cardComponent.ICard
import model.cardComponent.cardIm.randomCards
import model.gameComponent.IPlayerHand

case class PlayerHand @Inject() (cards: List[ICard] = randomCards(5)) extends IPlayerHand {
  def addCard(card: ICard): PlayerHand = copy(cards = card :: cards)

  def removeCard(card: ICard): PlayerHand = copy(cards = cards diff List(card))
}
