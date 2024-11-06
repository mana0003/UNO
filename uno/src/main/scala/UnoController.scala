// package src.main.scala.UnoController.scala
class UnoController(val game: UnoGame) {
  def playTurn(playerIndex: Int, card: Card): Boolean = {
    val player = game.players(playerIndex)
    if (game.playCard(player, card)) {
      println(s"${player.playerName} played ${card.color} ${card.value}")
      true
    } else {
      println(s"Invalid move by ${player.playerName}")
      false
    }
  }
}