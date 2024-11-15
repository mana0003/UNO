/*
import scala.io.StdIn
import scala.io.AnsiColor._

class TUI2(val controller: GameController) {
  controller.add(this)

  override def update(e: Event): Unit =
    e match {
      case Quit =>
        if (controller.round.players.exists(_.hand.cards.nonEmpty)) {
          // Game was quit prematurely, do not call gameOver()
        } else {
          // Game ended naturally, call gameOver()
          gameOver()
        }
      case _ => gameLoop()
    }

  def startGame(): Unit = {
    clearScreen()
    println("Welcome to Uno, the card game that destroys friendships!")
    displayMainMenu()
    println("Please enter a number from the menu above:")

    val selection = StdIn.readLine().toIntOption

    selection match {
      case Some(1) =>
        controller.initGame()
      case Some(2) =>
        println("Goodbye!")
        controller.quitGame()
      case _ =>
        println("Invalid input. Please enter a number between 1 and 2.") // Adjusted the number range
        startGame()
    }
  }

  private def displayMainMenu(): Unit = {
    val boxTopBottom = s"$BLUE" + "=" * 40 + s"$RESET"
    val menuItems = List("1. Start a new game", "2. Exit") // Removed "2. View the rules"
    val menuString = menuItems.mkString("\n")

    println(boxTopBottom)
    println(s"$BLUE||" + " " * 36 + "||")
    menuString.split("\n").foreach { item =>
      val paddingSize = (36 - item.length) / 2
      val line =
        s"$BLUE||" + " " * paddingSize + s"$CYAN" + item + " " * (36 - paddingSize - item.length) + s"$BLUE||"
      println(line)
    }
    println(s"$BLUE||" + " " * 36 + s"||")
    println(boxTopBottom)
  }

  private def gameLoop(): Unit = {
    clearScreen()
    val currentPlayer = controller.round.players(controller.round.currentPlayer)
    println(s"Current player: Player ${controller.round.currentPlayer + 1}")
    println(s"Current top card: ${controller.round.topCard.getColorCode}${controller.round.topCard.value}$RESET")
    currentPlayer.hand.cards.zipWithIndex.foreach { case (card, index) =>
      println(s"${index + 1}: ${card.getColorCode}${card.value}$RESET")
    }
    println("Enter the number of the card you want to play:")
    val cardNumber = StdIn.readLine().toIntOption
    if (cardNumber.isEmpty) {
      println("Invalid input. Please enter a number.")
      gameLoop()
    } else {
      handleGameMenuInput(cardNumber.get, currentPlayer.hand.cards.length)
    }
  }

  private def handleGameMenuInput(input: Int, handSize: Int): Unit = {
    if (input < 1 || input > handSize) {
      println("Invalid card number. Please try again.")
      gameLoop()
    } else {
      val card = controller.round.players(controller.round.currentPlayer).hand.cards(input - 1)
      if (controller.round.players(controller.round.currentPlayer).canPlay(card) && card.canBePlayedOn(controller.round.topCard)) {
        controller.playCard(card)
      } else {
        println("You can't play that card. Do you want to draw, or try again?")
        println("1: Draw a card")
        println("2: Try again")
        val selection = StdIn.readLine().toIntOption.getOrElse(-1)
        if (selection == -1) {
          println("Invalid input. Please enter a number.")
          gameLoop()
        } else if (selection == 1) {
          controller.drawCard()
        } else if (selection == 2) {
          gameLoop()
        } else {
          println("Invalid input. Please enter 1 or 2. Try again.")
          gameLoop()
        }
      }
    }
  }

  private def clearScreen(): Unit = {
    print("\u001b[H\u001b[2J")
  }

  private def gameOver(): Unit = {
    clearScreen()
    println("Game over!")
    val winnerIndex = controller.round.players.indexWhere(_.hand.cards.isEmpty)
    println(s"Player ${winnerIndex + 1} wins!")
  }
}
*/
