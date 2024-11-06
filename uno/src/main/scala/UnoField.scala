// src/main/scala/UnoField.scala
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