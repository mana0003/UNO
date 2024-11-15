/*class UnoController2(var round: Round) {


  /*def initGame(): Unit =
    notifyObservers(Event.Start)

  def quitGame(): Unit =
    notifyObservers(Event.Quit)
*/
  def playCard(card: Card): Unit = {

    val newPlayer = round.players(round.currentPlayer).playCard(card).get
    val newPlayers = round.players.updated(round.currentPlayer, newPlayer)
    if (newPlayer.hand.cards.isEmpty) {
      quitGame()
      return
    }
    round = round.copy(
      players = newPlayers,
      topCard = card,
      currentPlayer = (round.currentPlayer + 1) % round.players.length
    )
    notifyObservers(Event.Play)
  }

  def drawCard(): Unit = {

    val newCard = randomCard
    val newPlayer = Player(
      round.currentPlayer,
      round.players(round.currentPlayer).hand.addCard(newCard)
    )
    val newPlayers = round.players.updated(round.currentPlayer, newPlayer)
    round = round.copy(players = newPlayers)
    notifyObservers(Event.Draw)
  }
}*/