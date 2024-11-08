// src/test/scala/TUITest.scala
import scala.util.Try
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class TUITest extends AnyFunSuite with MockitoSugar {
  test("TUI main runs without errors") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))

    assert(Try(TUI.main(Array())).isSuccess) //{
      //TUI.main(Array())
    //}
  }
}