import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class MainTest extends AnyFunSuite with MockitoSugar {

  test("Main should initialize and run the game") {
    val tui = new TUI
    // Mock the UnoGame and TUI
    val mockGame = mock[UnoGame]
    val mockTUI = mock[tui.type]

    // Stub methods
    when(mockGame.discardPile).thenReturn(List(Card("Red", "5")))
    when(mockGame.players).thenReturn(Array(PlayerHand("Player 1", Array()), PlayerHand("Player 2", Array())))
    when(mockGame.checkWinner()).thenReturn(None).thenReturn(Some("Player 1"))

    // Create a new Main object with mocked dependencies
    object TestMain {
      val game = mockGame
      val TUI = mockTUI

      def main(args: Array[String]): Unit = {
        // Start the game with player names
        game.startGame(Array("Player 1", "Player 2"))

        // Display the initial game state
        val unoField = UnoField(game.discardPile.head, game.players)
        println(unoField.displayField())

        // Run the text-based user interface
        while (game.checkWinner().isEmpty) {
          TUI.main(args)
        }
      }
    }

    // Run the main method
    TestMain.main(Array())

    // Verify interactions
    verify(mockGame).startGame(Array("Player 1", "Player 2"))
    verify(mockTUI, times(1)).main(Array())
  }
}