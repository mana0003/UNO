import org.scalatest.funsuite.AnyFunSuite

class UnoControllerTest extends AnyFunSuite {
  test("playTurn with valid move updates game state") {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val controller = new UnoController(game)
    val player = game.players.head
    val card = player.cards.head
    assert(controller.playTurn(0, card))
    assert(game.discardPile.head == card)
    assert(!player.cards.contains(card))
  }

  test("playTurn with invalid move does not update game state") {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val controller = new UnoController(game)
    val invalidCard = Card("InvalidColor", "InvalidValue")
    assert(!controller.playTurn(0, invalidCard))
    assert(game.discardPile.head != invalidCard)
  }
}