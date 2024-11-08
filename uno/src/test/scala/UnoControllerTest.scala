import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class UnoControllerTest extends AnyFunSuite with BeforeAndAfter {

  var game: UnoGame = null
  var controller: UnoController = null
  val outContent = new ByteArrayOutputStream()
  val originalOut = System.out

  before {
    game = new UnoGame()
    controller = new UnoController(game)
    System.setOut(new PrintStream(outContent, true))
  }

  after {
    System.setOut(originalOut)
    outContent.reset()
  }

  test("playTurn with a valid move prints the correct message and continues the game") {
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    // game.discardPile ::= Card("Yellow", "2") // Ensure there's a compatible top card

    val card = player.cards.find(game.isValidMove).getOrElse(player.cards.head)

    assert(controller.playTurn(0, card))
    assert(outContent.toString.contains(s"${player.playerName} played ${card.color} ${card.value}"))
    //
  }

  test("playTurn with a valid move and a winning player prints the winner message") {
    game.players = Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array()))
    val player = game.players.head
    val card = player.cards.head

    assert(controller.playTurn(0, card))
    assert(outContent.toString.contains(s"${player.playerName} played ${card.color} ${card.value}"))
    assert(outContent.toString.contains(s"${player.playerName} wins the game!"))
  }

  test("playTurn with an invalid move prints the invalid move message") {
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    val card = Card("Invalid", "Card")

    assert(!controller.playTurn(0, card))
    assert(outContent.toString.contains(s"Invalid move by ${player.playerName}"))
  }
}
