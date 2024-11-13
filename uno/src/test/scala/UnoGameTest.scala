import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.concurrent.TimeLimits
import org.scalatest.time.Span
import org.scalatest.time.Seconds

class UnoGameTest extends AnyFunSuite with TimeLimits {

  test("startGame initializes players and discardPile") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame()
      game.startGame(Array("Player 1", "Player 2"))
      assert(game.players.length == 2)
      assert(game.discardPile.nonEmpty)
    }
  }

  test("randomPlayerHand should create a PlayerHand with the specified number of cards") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val playerName = "Player 1"
      val numCards = 5

      val playerHand = game.randomPlayerHand(playerName, numCards)

      assert(playerHand.playerName == playerName)
      assert(playerHand.cards.length == numCards)
      playerHand.cards.foreach(card => assert(card.isInstanceOf[Card]))
    }
  }

  test("playCard with valid move updates discardPile and player hand") {
  failAfter(Span(10, Seconds)) {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    val card = player.cards.head

    val initialDiscardPileSize = game.discardPile.size
    val initialPlayerHandSize = player.cards.size

    assert(game.playCard(player, card))
    assert(game.discardPile.head == card)
    assert(!player.cards.contains(card))
    assert(game.discardPile.size == initialDiscardPileSize + 1)
    assert(player.cards.size == initialPlayerHandSize - 1)
  }
}

  test("applyCardEffect should handle 'Draw' card effect") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val player = PlayerHand("Player 1", Array(Card("Red", "5")))
      game.players = Array(player, PlayerHand("Player 2", Array()))
      game.currentPlayerIndex = 0

      game.applyCardEffect(Card("Red", "Draw"))

      assert(game.players(1).cards.length == 2)
    }
  }

  test("applyCardEffect should handle 'Wild Draw Four' card effect") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val player = PlayerHand("Player 1", Array(Card("Red", "5")))
      game.players = Array(player, PlayerHand("Player 2", Array()))
      game.currentPlayerIndex = 0

      game.applyCardEffect(Card("", "Wild Draw Four"))

      assert(game.players(1).cards.length == 4)
    }
  }

  test("applyCardEffect should handle 'Wild' card effect") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame {
        override def randomCard(): Card = Card("Red", "5")
      }
      val player = PlayerHand("Player 1", Array(Card("Red", "5")))
      game.players = Array(player)
      game.discardPile = List(Card("", "Wild"))

      // Mock user input for color choice
      val input = new java.io.ByteArrayInputStream("Blue\n".getBytes)
      Console.withIn(input) {
        game.applyCardEffect(Card("", "Wild"))
      }

      assert(game.discardPile.head.color == "Blue")
    }
  }

  test("applyCardEffect should handle 'Skip' card effect") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val player = PlayerHand("Player 1", Array(Card("Red", "5")))
      game.players = Array(player, PlayerHand("Player 2", Array()))
      game.currentPlayerIndex = 0

      game.applyCardEffect(Card("Red", "Skip"))

      assert(game.currentPlayerIndex == 0)
    }
  }

  test("applyCardEffect should handle 'Reverse' card effect") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val player = PlayerHand("Player 1", Array(Card("Red", "5")))
      game.players = Array(player, PlayerHand("Player 2", Array()))
      game.currentPlayerIndex = 0

      game.applyCardEffect(Card("Red", "Reverse"))

      assert(game.direction == -1)
    }
  }

  test("playCard with invalid move does not update discardPile or player hand") {
  failAfter(Span(10, Seconds)) {
    val game = new UnoGame()
    game.startGame(Array("Player 1", "Player 2"))
    val player = game.players.head
    val invalidCard = Card("InvalidColor", "InvalidValue")
    assert(!game.playCard(player, invalidCard))
    assert(game.discardPile.head != invalidCard)
    assert(!player.cards.contains(invalidCard))
  }
}

  test("drawCards should add the specified number of cards to the player's hand") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame
      val playerName = "TestPlayer"
      val initialCards = Array(Card("Red", "5"))
      val player = PlayerHand(playerName, initialCards)
      game.players = Array(player)
      val numCardsToDraw = 3

      game.drawCards(0, numCardsToDraw)

      assert(game.players(0).cards.length == initialCards.length + numCardsToDraw)
    }
  }

  test("randomCard generates a valid card") {
    failAfter(Span(10, Seconds)) {
      val game = new UnoGame()
      val card = game.randomCard()
      assert(card.color.isEmpty || Array("Red", "Blue", "Green", "Yellow").contains(card.color))
      assert((0 to 9).map(_.toString).contains(card.value) || Array("Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four").contains(card.value))
    }
  }
  test("checkWinner returns the player with an empty hand") {
    val game = new UnoGame()
    val player1 = PlayerHand("Player 1", Array(Card("Red", "5")))
    val player2 = PlayerHand("Player 2", Array())
    game.players = Array(player1, player2)

    val winner = game.checkWinner()
    assert(winner.isDefined)
    assert(winner.get.playerName == "Player 2")
  }

  test("checkWinner returns None if no player has an empty hand") {
    val game = new UnoGame()
    val player1 = PlayerHand("Player 1", Array(Card("Red", "5")))
    val player2 = PlayerHand("Player 2", Array(Card("Blue", "7")))
    game.players = Array(player1, player2)

    val winner = game.checkWinner()
    assert(winner.isEmpty)
  }
}