import scala.util.Random

// Case class to represent a Card
case class Card(color: String, value: String)

// Case class to represent a Player's hand
case class PlayerHand(playerName: String, cards: Array[Card])

// Case class to represent the field
case class UnoField(discardTop: Card, playerHands: Array[PlayerHand]) {
  def displayField(): String = {
    val separator = "=" * 70
    val title = "                                 UNO"
    // Format the discard top card
    val discard = s"Discard Top Card: [${discardTop.color} ${discardTop.value}]"

    // Format all player hands
    val hands = playerHands
      .map { hand =>
        val cards = hand.cards
          .map(card => s"[${card.color} ${card.value}]")
          .mkString(", ")
        s"${hand.playerName} Hand: $cards"
      }
      .mkString("\n")

    // Return the final playing field as a string
    s"""
       |$separator
       |$title
       |$separator
       |
       |$discard
       |
       |$hands
       |
       |$separator
    """.stripMargin
  }
}

// Possible card colors and values
val colors = Array("Red", "Blue", "Green", "Yellow")
val values = (0 to 9).map(_.toString).toArray // Numbers 0 to 9

// Function to generate a random card
def randomCard(): Card = {
  val color = colors(Random.nextInt(colors.length))
  val value = values(Random.nextInt(values.length))
  Card(color, value)
}

// Function to generate a random player hand with a specified number of cards
def randomPlayerHand(playerName: String, numberOfCards: Int): PlayerHand = {
  val cards = Array.fill(numberOfCards)(randomCard())
  PlayerHand(playerName, cards)
}

// Generate random player hands
val player1Hand = randomPlayerHand("Player 1", 5) // 5 random cards for Player 1
val player2Hand = randomPlayerHand("Player 2", 5) // 5 random cards for Player 2

// Create the Uno field
val discardTop = randomCard() // Random card for the discard top
val unoField = UnoField(discardTop, Array(player1Hand, player2Hand))

// Display the playing field
println(unoField.displayField())

