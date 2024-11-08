class UnoGame {
  var players: Array[PlayerHand] = Array()
  var discardPile: List[Card] = List()
  var currentPlayerIndex: Int = 0
  var direction: Int = 1 // 1 for clockwise, -1 for counterclockwise

  def startGame(playerNames: Array[String]): Unit = {
    players = playerNames.map(name => PlayerHand(name, Array.fill(5)(randomCard())))  // make new method for this
    discardPile = List(randomCard())            // make new method for this
  }
  def randomPlayerHand(playerName: String, numCards: Int): PlayerHand = {
    PlayerHand(playerName, Array.fill(numCards)(randomCard()))
  }

  def playCard(player: PlayerHand, card: Card): Boolean = {
    if (isValidMove(card)) {
      discardPile = card :: discardPile
      player.cards = player.cards.filterNot(_ == card)
      applyCardEffect(card)
      true
    } else {
      false
    }
  }

  def isValidMove(card: Card): Boolean = {
  val topCard = discardPile.head
  println(s"Top card: ${topCard.color} ${topCard.value}")
  card.color == topCard.color || card.value == topCard.value || card.value.startsWith("Wild") || card.value == "Draw"
  true
}

  def applyCardEffect(card: Card): Unit = {
    card.value match {
      case "Draw" => drawCards(nextPlayerIndex(), 2)
      case "Wild Draw Four" => drawCards(nextPlayerIndex(), 4)
      case "Wild" =>
        println("Choose a color (Red, Blue, Green, Yellow):")
        val newColor = scala.io.StdIn.readLine().trim
        discardPile = Card(newColor, card.value) :: discardPile.tail
      case _ => // No special effect
    }
    if (card.value == "Skip") {
      currentPlayerIndex = (currentPlayerIndex + direction * 2) % 2
    } else if (card.value == "Reverse") {
      direction *= -1
    } else {
      currentPlayerIndex = (currentPlayerIndex + direction) % players.length
    }

  }

  def drawCards(playerIndex: Int, count: Int): Unit = {
    val player = players(playerIndex)
    player.cards ++= Array.fill(count)(randomCard())
  }

  def nextPlayerIndex(): Int = {
    (currentPlayerIndex + direction) % players.length
  }

  def randomCard(): Card = {
    val colors = Array("Red", "Blue", "Green", "Yellow")
    val values = (0 to 9).map(_.toString).toArray ++ Array("Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four")
    val value = values(scala.util.Random.nextInt(values.length))
    val color = if (value.startsWith("Wild")) "" else colors(scala.util.Random.nextInt(colors.length))
    Card(color, value)
  }

  def checkWinner(): Option[PlayerHand] = {
    players.find(_.cards.isEmpty)
  }
}