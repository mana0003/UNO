import org.scalatest.wordspec.AnyWordSpec

class UnoGameSpec extends AnyWordSpec {

  "A Card" should {
    "have the correct color and value when created" in {
      val card = Card("Red", "5")
      assert(card.color == "Red")
      assert(card.value == "5")
    }
  }

  "A PlayerHand" should {
    "contain the correct number of cards" in {
      val cards = Array(Card("Red", "5"), Card("Blue", "2"))
      val hand = PlayerHand("Player 1", cards)
      assert(hand.playerName == "Player 1")
      assert(hand.cards.length == 2)
      assert(hand.cards.head == Card("Red", "5"))
    }
  }

  "randomPlayerHand" should {
    "generate a hand with 5 cards" in {
      val playerHand = Main.randomPlayerHand("Player 1", 5)
      assert(playerHand.playerName == "Player 1")
      assert(playerHand.cards.length == 5)
    }
  }

  "UnoField" should {
    "display the field correctly" in {
      val discardTop = Card("Yellow", "7")
      val player1Hand = PlayerHand("Player 1", Array(Card("Red", "5"), Card("Blue", "2")))
      val player2Hand = PlayerHand("Player 2", Array(Card("Green", "9"), Card("Yellow", "1")))

      val unoField = UnoField(discardTop, Array(player1Hand, player2Hand))
      val fieldDisplay = unoField.displayField()

      assert(fieldDisplay.contains("Discard Top Card: [Yellow 7]"))
      assert(fieldDisplay.contains("Player 1 Hand: [Red 5], [Blue 2]"))
      assert(fieldDisplay.contains("Player 2 Hand: [Green 9], [Yellow 1]"))
    }
  }
}
