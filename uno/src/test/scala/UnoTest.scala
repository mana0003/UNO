import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec

class UnoTest extends AnyWordSpec with MockitoSugar {
  "An Uno object" when {
    "new" should {
      val game = new UnoGame()
      val tui = new TUI()
      "have a game object" in {
        assert(game.isInstanceOf[UnoGame])
      }
      "have a main method" in {
        assert(tui.isInstanceOf[TUI])
      }
    }
  }
}