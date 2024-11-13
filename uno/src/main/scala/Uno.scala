object Uno extends App {
  // Initialize the game
  val game = new UnoGame()

  // Start the game with player names
  // game.startGame(Array("Player 1", "Player 2"))

  // Display the initial game state
  // val unoField = UnoField(game.discardPile.head, game.players)
  // println(unoField.displayField())
  val emptyArray = Array("")
  val tui = new TUI()
  // Run the text-based user interface
  while (game.checkWinner().isEmpty) {
    tui.main(emptyArray)
  }
}