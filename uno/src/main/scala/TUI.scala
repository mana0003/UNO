// src/main/scala/TUI.scala
object TUI {
  def main(args: Array[String]): Unit = {
    val controller = new UnoController(new UnoGame())
    controller.game.startGame(Array("Player 1", "Player 2"))
    var currentPlayerIndex = 0

    while (true) {
      val currentPlayer = controller.game.players(currentPlayerIndex)
      val topCard = controller.game.discardPile.head
      println(s"Top card on deck: [${topCard.color} ${topCard.value}]")
      println(s"${currentPlayer.playerName}'s turn. Your hand: ${currentPlayer.cards.mkString(", ")}")
      println("Enter the card to play (e.g., Red 5) or 'draw' to draw a card:")
      val input = scala.io.StdIn.readLine().trim

      if (input == "draw") {
        currentPlayer.cards = currentPlayer.cards :+ controller.game.randomCard()
      } else {
        val parts = input.split(" ")
        val cardOption = if (parts.length == 2) {
          Some(Card(parts(0), parts(1)))
        } else if (parts.length == 1 && (parts(0) == "Wild" || parts(0) == "Wild Draw Four")) {
          Some(Card("", parts(0)))
        } else {
          println("Invalid input. Try again.")
          None
        }

        cardOption match {
          case Some(card) =>
            if (!controller.playTurn(currentPlayerIndex, card)) {
              println("Invalid move. Try again.")
            }
          case None => // Do nothing
        }
      }

      currentPlayerIndex = (currentPlayerIndex + 1) % controller.game.players.length
    }
  }
}