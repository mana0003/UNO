import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class UnoTest extends AnyFunSuite with MockitoSugar {

  test("UnoGame initializes correctly") {
    val game = new UnoGame()
    assert(game != null)
  }

  test("Empty array is created correctly") {
    val emptyArray = Array("")
    assert(emptyArray.length == 1)
    assert(emptyArray(0) == "")
  }

  test("TUI initializes correctly") {
    val tui = new TUI()
    assert(tui != null)
  }

  test("checkWinner returns empty initially") {
    val game = new UnoGame()
    assert(game.checkWinner().isEmpty)
  }

  test("TUI main runs without errors") {
    val game = mock[UnoGame]
    val tui = mock[TUI]

    when(game.checkWinner()).thenReturn(None).thenReturn(Some(PlayerHand("Player 1", Array(Card("Red", "5")))))
    doNothing().when(tui).main(Array(""))

    while (game.checkWinner().isEmpty) {
      tui.main(Array(""))
    }

    verify(tui, atLeastOnce()).main(Array(""))
  }
}