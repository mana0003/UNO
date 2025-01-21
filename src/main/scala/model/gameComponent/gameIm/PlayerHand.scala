package model.gameComponent.gameIm
import com.google.inject.Inject
import model.cardComponent.cardIm.*
import model.cardComponent.{ICard, cardColors, cardValues}
import model.gameComponent.IPlayerHand

case class PlayerHand @Inject (cards: List[ICard] = randomCards(5)) extends IPlayerHand {
  def addCard(card: ICard): PlayerHand = copy(cards = card :: cards)

  def removeCard(card: ICard): PlayerHand = {
    println(s"Removing card: ${card.getValue} of color ${card.getColor}")
    val remaining = cards.filterNot(c =>
      (c.getValue == card.getValue && (c.getColor == card.getColor || card.getValue == cardValues.WILD || card.getValue == cardValues.WILD_DRAW_FOUR))
    )
    println(s"Remaining cards: ${remaining.map(c => s"${c.getValue} of color ${c.getColor}")}")
    copy(cards = remaining)
  }
  def toXml: scala.xml.Node = {
    <playerHand>
      {cards.map(_.toXml)}
    </playerHand>
  }
}