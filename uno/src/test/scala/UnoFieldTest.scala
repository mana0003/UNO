import org.scalatest.funsuite.AnyFunSuite

class UnoFieldTest extends AnyFunSuite {
  test("displayField returns correct string representation") {
    val card = Card("Red", "5")
    val playerHands = Array(PlayerHand("Player 1", Array(card)), PlayerHand("Player 2", Array(card)))
    val unoField = UnoField(card, playerHands)
    val fieldString = unoField.displayField()
    assert(fieldString.contains("UNO"))
    assert(fieldString.contains("Discard Top Card: [Red 5]"))
    assert(fieldString.contains("Player 1 Hand: [Red 5]"))
    assert(fieldString.contains("Player 2 Hand: [Red 5]"))
  }
}