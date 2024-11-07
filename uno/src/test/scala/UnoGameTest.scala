// src/test/scala/UnoGameTest.scala
import org.scalatest.funsuite.AnyFunSuite

class UnoGameTest extends AnyFunSuite {
  test("startGame initializes players and discardPile") {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    assert(game.players.length == 2)
    assert(game.discardPile.nonEmpty)
  }

  test("playCard with valid move updates discardPile and player hand") {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    val card = player.cards.head
    assert(game.playCard(player, card))
    assert(game.discardPile.head == card)
    assert(!player.cards.contains(card))
  }

  test("playCard with invalid move does not update discardPile or player hand") {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    val invalidCard = Card("InvalidColor", "InvalidValue")
    assert(!game.playCard(player, invalidCard))
    assert(game.discardPile.head != invalidCard)
    assert(!player.cards.contains(invalidCard))
  }

  test("randomCard generates a valid card") {
    val game = new UnoGame()
    val card = game.randomCard()
    assert(card.color.isEmpty || Array("Red", "Blue", "Green", "Yellow").contains(card.color))
    assert((0 to 9).map(_.toString).contains(card.value) || Array("Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four").contains(card.value))
  }
}