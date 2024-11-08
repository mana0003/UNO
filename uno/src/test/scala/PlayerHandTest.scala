import org.scalatest.funsuite.AnyFunSuite

class PlayerHandTest extends AnyFunSuite {

  test("PlayerHand initializes correctly") {
    val playerHand = PlayerHand("Player 1", Array(Card("Red", "5"), Card("Blue", "7")))
    assert(playerHand.playerName == "Player 1")
    assert(playerHand.cards.length == 2)
    assert(playerHand.cards.contains(Card("Red", "5")))
    assert(playerHand.cards.contains(Card("Blue", "7")))
  }

  test("PlayerHand allows updating cards") {
    val playerHand = PlayerHand("Player 1", Array(Card("Red", "5")))
    playerHand.cards = Array(Card("Green", "3"))
    assert(playerHand.cards.length == 1)
    assert(playerHand.cards.contains(Card("Green", "3")))
  }
}