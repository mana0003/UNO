object Uno extends App {
  val game = new UnoGame
  val controller = new UnoController(game)
  val tui = new TUI()
  tui.run()
}