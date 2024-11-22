package scala.model
case class PlayerHand(cards: List[Card] = randomCards(5)) {
  def addCard(card: Card): PlayerHand = copy(cards = card :: cards)

  def removeCard(card: Card): PlayerHand = copy(cards = cards diff List(card))
}