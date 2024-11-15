import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class UnoGameTest extends AnyWordSpec with Matchers {
  "A UnoGame" should {
    "initialize with the correct number of players" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.players.length should be (2)
    }

    "have a card on discard pile at the start" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile should not be (empty)
    }
    "playCard gets an invalid move" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Blue", "0"))
      val topCard = game.discardPile.head
      val currentPlayerIndex = game.currentPlayerIndex
      game.playCard(game.players(currentPlayerIndex), Card("Red", "5")) should be (false)
    }

    "correctly identify the winner" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      // Simulate a winning condition
      game.players(0).cards = Array()
      game.checkWinner().map(_.playerName) should be (Some("Player 1"))
    }
    "isValidMove gets the right color of the draw card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentPlayerIndex = game.currentPlayerIndex
      game.players(currentPlayerIndex).cards = Array(Card("Red", "Draw"))
      game.isValidMove(Card("Blue", "Draw")) should be (true)
    }
    "applyCardEffect correctly applies the effect of a Wild card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentPlayerIndex = game.currentPlayerIndex

      // Mock user input for choosing a color
      val inputStream = new java.io.ByteArrayInputStream("Red\n".getBytes)
      Console.withIn(inputStream) {
        game.playCard(game.players(currentPlayerIndex), Card("", "Wild"))
      }

      game.discardPile.head.color should be ("Red")

    }
    "applyCardEffect correctly applies the effect of a Draw card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentCardCount = game.players(game.nextPlayerIndex()).cards.length
      val currentPlayerIndex = game.currentPlayerIndex
      game.players(currentPlayerIndex).cards = Array(Card("Red", "Draw"))

      game.playCard(game.players(currentPlayerIndex), Card("Red", "Draw"))
      val newCardCount = currentCardCount + 2
      game.players(game.nextPlayerIndex()).cards.length should be (0)
    }
    "applyCardEffect correctly applies the effect of a Wild Draw Four card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentCardCount = game.players(game.nextPlayerIndex()).cards.length
      val currentPlayerIndex = game.currentPlayerIndex
      game.players(currentPlayerIndex).cards = Array(Card("", "Wild Draw Four"))

      game.playCard(game.players(currentPlayerIndex), Card("", "Wild Draw Four"))
      val newCardCount = currentCardCount + 4
      game.players(game.nextPlayerIndex()).cards.length should be (0)
    }
    "applyCardEffect correctly applies the effect of a Reverse card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentPlayerIndex = game.currentPlayerIndex
      val currentDirection = game.direction

      game.playCard(game.players(currentPlayerIndex), Card("Red", "Reverse"))
      game.direction should be (-currentDirection)
    }
    "applyCardEffect correctly applies the effect of a Skip card" in {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      game.discardPile = List(Card("Red", "0"))
      val currentPlayerIndex = game.currentPlayerIndex
      val currentDirection = game.direction

      game.playCard(game.players(currentPlayerIndex), Card("Red", "Skip"))
      game.currentPlayerIndex should be (0)
    }
  }

}