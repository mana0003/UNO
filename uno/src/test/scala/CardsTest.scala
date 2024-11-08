import org.scalatest.funsuite.AnyFunSuite

class CardsTest extends AnyFunSuite {

  test("Card should correctly identify special cards") {
    assert(Card("Red", "Skip").isSpecial)
    assert(Card("Blue", "Reverse").isSpecial)
    assert(Card("Green", "Draw Two").isSpecial)
    assert(Card("Yellow", "Wild").isSpecial)
    assert(Card("Red", "Wild Draw Four").isSpecial)
    assert(!Card("Red", "5").isSpecial)
    assert(!Card("Blue", "7").isSpecial)
  }

  test("Card should correctly store color and value") {
    val card = Card("Red", "5")
    assert(card.color == "Red")
    assert(card.value == "5")
  }
}