/*import scala.util.{Failure, Success, Try}

case class Player2(id: Int, hand: Hand) {
  def canPlay(card: Card): Boolean = {
    hand.cards.exists(_.canBePlayedOn(card))
  }

  def playCard(card: Card): Try[Player] = {
    if (canPlay(card)) {
      Success(Player(id, hand.removeCard(card)))
    } else {
      Failure(IllegalArgumentException("Can't play that card"))
    }
  }
}*/