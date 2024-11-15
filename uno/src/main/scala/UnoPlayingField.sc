enum cardColors:
  case RED, GREEN, BLUE, YELLOW

enum cardValues:
  case ZERO
  case ONE
  case TWO
  case THREE
  case FOUR
  case FIVE
  case SIX
  case SEVEN
  case EIGHT
  case NINE
  case SKIP
  case REVERSE
  case BLOCK
  case DRAW_TWO
  case WILD
  case WILD_DRAW_FOUR

case class Card(color: cardColors, value: cardValues)


// can card be played
def canPlayCard(card: Card, topCard: Card): Boolean = {
  card.color == topCard.color || card.value == topCard.value || card.value == cardValues.WILD || card.value == cardValues.WILD_DRAW_FOUR
}

val c1 = Card(cardColors.RED, cardValues.ONE)
assert(c1.color == cardColors.RED)
assert(c1.value == cardValues.ONE)
assert(c1.color != cardColors.GREEN)
assert(c1.value != cardValues.TWO)

// test can play card
val c2 = Card(cardColors.RED, cardValues.TWO)
val c3 = Card(cardColors.GREEN, cardValues.TWO)
val c4 = Card(cardColors.RED, cardValues.THREE)
val c5 = Card(cardColors.GREEN, cardValues.THREE)

assert(canPlayCard(c2, c1))
assert(!canPlayCard(c3, c1))
assert(canPlayCard(c4, c1))
assert(!canPlayCard(c5, c1))