class UnoController(val game: UnoGame) {

  def playTurn(playerIndex: Int, card: Card): Boolean = {
    // if the played card does not match the top card of the discard pile, return false
    if (!game.isValidMove(card)) {
      println(s"Invalid move")
      return false
    } else {
      // if the played card is a valid move, play the card
      game.playCard(game.players(playerIndex), card)
      print(s"${game.players(playerIndex).playerName} played ${card.color} ${card.value}")
      return true
    }
  }
}