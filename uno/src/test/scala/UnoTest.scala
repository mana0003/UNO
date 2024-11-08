import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class MainTest extends AnyFunSuite with MockitoSugar {

  test("Main should initialize and run the game") {
    // Mock the UnoGame and TUI
    val mockGame = mock[UnoGame]
    val mockTUI = mock[TUI.type]

    // Stub methods
    when(mockGame.discardPile).thenReturn(List(Card("Red", "5")))
    when(mockGame.players).thenReturn(Array(PlayerHand("Player 1", Array()), PlayerHand("Player 2", Array())))
    when(mockGame.checkWinner()).thenReturn(None).thenReturn(Some("Player 1"))

    // Create a new Main object with mocked dependencies
    object TestMain extends Main {
      override val game = mockGame
      override val TUI = mockTUI
    }

    // Run the main method
    TestMain.main(Array())

    // Verify interactions
    verify(mockGame).startGame(Array("Player 1", "Player 2"))
    verify(mockTUI, times(1)).main(Array())
  }
}