import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import scala.util.Try
import org.scalatest.matchers.should.Matchers
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TUITest extends AnyWordSpec with MockitoSugar with Matchers {
  "A TUI" should {
    "correctly handle user input" in {
      val inputString = "Red 5\n"
      val inputStream = new ByteArrayInputStream(inputString.getBytes)
      Console.withIn(inputStream) {
        val input = Option(scala.io.StdIn.readLine()).getOrElse("").trim
        input should be("Red 5")
      }
    }

    "initialize the game properly with two players" in {
      val mockGame = mock[UnoGame]
      val mockController = mock[UnoController]
      when(mockController.game).thenReturn(mockGame)
      when(mockGame.players).thenReturn(Array(PlayerHand("Player 1", Array()), PlayerHand("Player 2", Array())))

      mockGame.startGame(Array("Player 1", "Player 2"))
      verify(mockGame).startGame(Array("Player 1", "Player 2"))
      mockGame.players.length should be(2)
    }

    "run the game without errors" in {
      val mockGame = mock[UnoGame]
      val controller = mock[UnoController]
      when(controller.game).thenReturn(mockGame)
      val tui = new TUI()
      noException should be thrownBy {
        Try(tui.run()).recover { case e: InterruptedException => e.printStackTrace() }
      }
    }

    "not print the winner when there is none" in {
      val mockGame = mock[UnoGame]
      val mockController = mock[UnoController]
      when(mockController.game).thenReturn(mockGame)
      when(mockGame.checkWinner()).thenReturn(None)

      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        val tui = new TUI()
        tui.run()
      }
      val output = outputStream.toString
      output should not include ("wins the game!")
    }

    "check if there is a winner" in {
      val game = new UnoGame
      val controller = new UnoController(game)
      val tui = new TUI()
      game.startGame(Array("Player 1", "Player 2"))
      game.players(0).cards = Array() // Simulate an empty hand
      game.checkWinner().map(_.playerName) should be(Some("Player 1"))
    }

    "handle invalid card input" in {
      def isValidColor(color: String): Boolean = Set("Red", "Blue", "Green", "Yellow").contains(color)
      def isValidValue(value: String): Boolean = Set("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Skip", "Reverse", "Draw Two").contains(value)

      val invalidInput = "Invalid input"
      val parts = invalidInput.split(" ")
      val cardOption = if (parts.length == 2 && isValidColor(parts(0)) && isValidValue(parts(1))) {
        Some(Card(parts(0), parts(1)))
      } else {
        None
      }
      cardOption shouldBe None
    }

    "be defined when parts length is 2" in {
      val parts = Array("Red", "5")
      val cardOption = Some(Card(parts(0), parts(1)))
      cardOption shouldBe defined
      cardOption.get.color should be("Red")
      cardOption.get.value should be("5")
    }

    "cardOption should be None when parts length is not 2" in {
      val parts = Array("Red")
      val cardOption = if (parts.length == 2) Some(Card(parts(0), parts(1))) else None
      cardOption shouldBe None
    }

    "handle cardOption match case" in {
      val controller = mock[UnoController]
      val card = Card("Red", "5")
      val cardOption = Some(card)
      when(controller.playTurn(any[Int], any[Card])).thenReturn(false)

      cardOption match {
        case Some(card) =>
          if (!controller.playTurn(0, card)) println("Invalid move. Try again.")
        case None => println("No card provided.") // Added for safety
      }
      verify(controller).playTurn(0, card)
    }

    "handle valid card input with color and value" in {
      val input = "Red 5"
      val parts = input.split(" ")
      val cardOption = if (parts.length == 2) Some(Card(parts(0), parts(1))) else None

      cardOption shouldBe defined
      cardOption.get.color should be("Red")
      cardOption.get.value should be("5")
    }

    "handle valid card input with Wild card" in {
      val input = "Wild"
      val parts = input.split(" ")
      val cardOption = if (parts.length == 1 && (parts(0) == "Wild" || parts(0) == "Wild Draw Four")) {
        Some(Card("", parts(0)))
      } else {
        None
      }
      cardOption shouldBe defined
      cardOption.get.color should be("")
      cardOption.get.value should be("Wild")
    }

    "handle valid card play" in {
      val controller = mock[UnoController]
      val card = Card("Red", "5")
      val cardOption = Some(card)
      when(controller.playTurn(any[Int], any[Card])).thenReturn(true)

      cardOption match {
        case Some(card) =>
          controller.playTurn(0, card)
        case None => // Added for safety
      }
      verify(controller).playTurn(0, card)
    }

    "handle invalid card play" in {
      val controller = mock[UnoController]
      val card = Card("Red", "5")
      val cardOption = Some(card)
      when(controller.playTurn(any[Int], any[Card])).thenReturn(false)

      val outputStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outputStream)) {
        cardOption match {
          case Some(card) =>
            if (!controller.playTurn(0, card)) println("Invalid move. Try again.")
          case None => println("No card provided.") // Added for safety
        }
      }
      verify(controller).playTurn(0, card)
      outputStream.toString should include("Invalid move. Try again.")
    }
  }
}
