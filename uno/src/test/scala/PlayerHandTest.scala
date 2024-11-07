import org.scalatest.funsuite.AnyFunSuite

class PlayerHandTest extends AnyFunSuite {
  test("PlayerHand initializes correctly") {
    val card = Card("Red", "5")
    val playerHand = PlayerHand("Player 1", Array(card))
    assert(playerHand.playerName == "Player 1")
    assert(playerHand.cards.contains(card))
  }
}