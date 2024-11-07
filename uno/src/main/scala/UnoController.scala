class UnoController(val game: UnoGame) {
  def playTurn(playerIndex: Int, card: Card): Boolean = {
    val player = game.players(playerIndex)
    if (game.playCard(player, card)) {
      println(s"${player.playerName} played ${card.color} ${card.value}")
      game.checkWinner() match {
        case Some(winner) => println(s"${winner.playerName} wins the game!")
        case None => // Continue the game
      }
      true
    } else {
      println(s"Invalid move by ${player.playerName}")
      false
    }
  }
}