package model.gameComponent.gameIm
import com.google.inject.Inject
import model.cardComponent.cardIm.*
import model.cardComponent.{ICard, cardColors, cardValues}
import model.gameComponent.IPlayerHand

case class PlayerHand @Inject (cards: List[ICard] = randomCards(5)) extends IPlayerHand {
  def addCard(card: ICard): PlayerHand = copy(cards = card :: cards)

  def removeCard(card: ICard): PlayerHand = {
    val (removed, remaining) = cards.partition(c => c.getValue == card.getValue && c.getColor == card.getColor)
    copy(cards = remaining)
  }
  def toXml: scala.xml.Node = {
    <playerHand>
      {cards.map(_.toXml)}
    </playerHand>
  }
}