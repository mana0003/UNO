// src/main/scala/UnoGame.scala
class UnoGame {
  var players: Array[PlayerHand] = Array()
  var discardPile: List[Card] = List()

  def startGame(playerNames: Array[String]): Unit = {
    players = playerNames.map(name => PlayerHand(name, Array.fill(5)(randomCard())))
    discardPile = List(randomCard())
  }

  def playCard(player: PlayerHand, card: Card): Boolean = {
    if (isValidMove(card)) {
      discardPile = card :: discardPile
      player.cards = player.cards.filterNot(_ == card)
      if (card.value == "Wild" || card.value == "Wild Draw Four") {
        println("Choose a color (Red, Blue, Green, Yellow):")
        val newColor = scala.io.StdIn.readLine().trim
        discardPile = Card(newColor, card.value) :: discardPile.tail
      }
      true
    } else {
      false
    }
  }

  def isValidMove(card: Card): Boolean = {
    val topCard = discardPile.head
    card.color == topCard.color || card.value == topCard.value || card.value.startsWith("Wild")
  }

  def randomCard(): Card = {
    val colors = Array("Red", "Blue", "Green", "Yellow")
    val values = (0 to 9).map(_.toString).toArray ++ Array("Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four")
    val color = colors(scala.util.Random.nextInt(colors.length))
    val value = values(scala.util.Random.nextInt(values.length))
    Card(color, value)
  }
}