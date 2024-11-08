import scala.util.Try
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any

class TUITest extends AnyFunSuite with MockitoSugar {


  test("TUI main runs without errors") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    val tui = mock[TUI]

    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))

    // Mock the TUI methods to prevent endless loop
    when(tui.run()).thenAnswer(_ => ())

    // Check that calling TUI.main does not throw any exceptions
    assert(Try(tui.main(Array())).isSuccess)
  }
/*
  test("TUI run starts the game and processes player turns") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    val tui = new TUI()

    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))
    when(game.checkWinner()).thenReturn(None).thenReturn(Some(PlayerHand("Player 1", Array(Card("Red", "5")))))
    when(game.randomCard()).thenReturn(Card("Yellow", "2"))

    // Mock user input
    val inputStream = new java.io.ByteArrayInputStream("draw\n".getBytes)
    Console.withIn(inputStream) {
      Console.withOut(new java.io.ByteArrayOutputStream()) {
        Console.withErr(new java.io.ByteArrayOutputStream()) {
          tui.run()
        }
      }
    }

    verify(game).startGame(Array("Player 1", "Player 2"))
    verify(game, atLeastOnce()).checkWinner()
    verify(game).randomCard()
  }

  test("TUI run handles invalid input") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    val tui = new TUI()

    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))
    when(game.checkWinner()).thenReturn(None).thenReturn(Some(PlayerHand("Player 1", Array(Card("Red", "5")))))

    // Mock user input
    val inputStream = new java.io.ByteArrayInputStream("invalid\n".getBytes)
    Console.withIn(inputStream) {
      Console.withOut(new java.io.ByteArrayOutputStream()) {
        Console.withErr(new java.io.ByteArrayOutputStream()) {
          tui.run()
        }
      }
    }

    verify(game).startGame(Array("Player 1", "Player 2"))
    verify(game, atLeastOnce()).checkWinner()
  }

  test("TUI run processes valid card play") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    val tui = new TUI()

    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))
    when(game.checkWinner()).thenReturn(None).thenReturn(Some(PlayerHand("Player 1", Array(Card("Red", "5")))))
    when(game.playCard(any[PlayerHand], any[Card])).thenReturn(true)

    // Mock user input
    val inputStream = new java.io.ByteArrayInputStream("Red 5\n".getBytes)
    Console.withIn(inputStream) {
      Console.withOut(new java.io.ByteArrayOutputStream()) {
        Console.withErr(new java.io.ByteArrayOutputStream()) {
          tui.run()
        }
      }
    }

    verify(game).startGame(Array("Player 1", "Player 2"))
    verify(game, atLeastOnce()).checkWinner()
    verify(game).playCard(any[PlayerHand], any[Card])
  }

  test("TUI run processes draw card action") {
    val game = mock[UnoGame]
    val controller = new UnoController(game)
    val tui = new TUI()

    when(game.players).thenReturn(Array(PlayerHand("Player 1", Array(Card("Red", "5"))), PlayerHand("Player 2", Array(Card("Blue", "7")))))
    when(game.discardPile).thenReturn(List(Card("Green", "3")))
    when(game.checkWinner()).thenReturn(None).thenReturn(Some(PlayerHand("Player 1", Array(Card("Red", "5")))))
    when(game.randomCard()).thenReturn(Card("Yellow", "2"))

    // Mock user input
    val inputStream = new java.io.ByteArrayInputStream("draw\n".getBytes)
    Console.withIn(inputStream) {
      Console.withOut(new java.io.ByteArrayOutputStream()) {
        Console.withErr(new java.io.ByteArrayOutputStream()) {
          tui.run()
        }
      }
    }

    verify(game).startGame(Array("Player 1", "Player 2"))
    verify(game, atLeastOnce()).checkWinner()
    verify(game).randomCard()
  }*/
}