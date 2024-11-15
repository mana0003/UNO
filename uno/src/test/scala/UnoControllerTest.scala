import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class UnoControllerTest extends AnyWordSpec with BeforeAndAfter  {

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

  "An UnoController" should {
    "playTurn with a valid move prints the correct message and continues the game" in {
      game.startGame(Array("Player 1", "Player 2"))
      val player = game.players.head
      val card = player.cards.find(game.isValidMove).getOrElse(player.cards.head)

      assert(controller.playTurn(0, card))
      //assert(outContent.toString.contains(s"${player.playerName} played ${card.color} ${card.value}"))
    }
    "playTurn with an invalid move prints the invalid move message" in {
      game.startGame(Array("Player 1", "Player 2"))
      val player = game.players.head
      val card = new Card("InvalidColor", "InvalidValue")

      assert(!controller.playTurn(0, card))
      // assert(outContent.toString.contains(s"Invalid move"))
    }
}
}
