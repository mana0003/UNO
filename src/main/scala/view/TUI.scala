package view

import controller.controllerComponent.IUnoController
import util.{Event, Observer}

import scala.io.StdIn
import scala.io.AnsiColor.*
import controller.patterns.UnoActionBuilder

import scala.annotation.tailrec

class TUI(val controller: IUnoController) extends Observer{
  // controller.addObserver(this)

  private def processInputLine(input: Int, handSize: Int): Unit = {
    if (input < 1 || input > handSize) {
      println("Invalid card number. Please try again.")
      gameContinue()
    } else {
      val card = controller.getField.players(controller.getCurrentPlayer).hand.cards(input - 1)
      if (controller.getField.players(controller.getCurrentPlayer).valid(card) && card.canBePlayed(controller.getField.topCard)) {
        UnoActionBuilder.builder().setAction("play").setCard(card).build().executeAction(controller, controller.getField.players(controller.getCurrentPlayer))
      } else {
        println("Card does not fit. Do you want to:")
        println("1. Draw a card")
        println("2. Try again")
        println("3. Undo")
        println("4. Redo")
        println("5. Quit")
        val readLine = StdIn.readLine().toIntOption.getOrElse(-1)
        readLine match {
          case 1 => UnoActionBuilder.builder().setAction("draw").build().executeAction(controller, controller.getField.players(controller.getCurrentPlayer))
          case 2 => gameContinue()
          case 3 => undoAction()
          case 4 => redoAction()
          case 5 => controller.notifyObservers(Event.Quit)
          case _ =>
            println("Invalid input. Please enter a number.")
            gameContinue()
        }
      }
    }
  }

  override def update(e: Event): Unit = {
    if (controller.isGuiMode) {
      return
    }
    e match {
      case Event.Start =>
        println("Game Started!")
        gameContinue()
      case Event.Play =>
        println("Card played!")
        gameContinue()
      case Event.Draw =>
        println("Card drawn!")
        gameContinue()
      case Event.Undo =>
        println("Undo action performed!")
        gameContinue()
      case Event.Redo =>
        println("Redo action performed!")
        gameContinue()
      case Event.Error =>
        println("An error occurred!")
        gameContinue()
      case Event.Quit =>
        gameOver()
    }
  }

  def startGame(): Unit = {
    displayField()
    println("Enter a number from the menu above: ")
    val readLine = StdIn.readLine().toIntOption
    readLine match {
      case Some(1) =>
        controller.notifyObservers(Event.Start)
      case Some(2) =>
        println("Goodbye!")
        controller.notifyObservers(Event.Quit)
      case _ =>
        println("Invalid input. Please enter a number between 1 and 2.")
        startGame()
    }
  }

  private def gameOver(): Unit = {
    println("Game over!")
    val winnerIndex = controller.getField.players.indexWhere(_.hand.cards.isEmpty)
    println(s"Player ${winnerIndex + 1} wins!")
  }

  private def displayField(): Unit = {
    val separator = "=" * 60
    val title = "                           UNO"
    val options = List("1. Start playing UNO", "2. Quit")
    val menu =
      s"""
         |$separator
         |
         |$title
         |
         |$separator
      """.stripMargin

    println(menu)
    println(options.mkString("\n"))
  }

  @tailrec
  private def gameContinue(): Unit = {
    val currentPlayer = controller.getField.players(controller.getCurrentPlayer)
    println(s"Current player: Player ${controller.getCurrentPlayer + 1}")
    println(s"Current top card: ${controller.getField.topCard.getColorCode}${controller.getField.topCard.getValue}$RESET")
    currentPlayer.hand.cards.zipWithIndex.foreach { case (card, index) =>
      println(s"${index + 1}: ${card.getColorCode}${card.getValue}$RESET")
    }
    println("Enter the number of the card you want to play:")
    val cardNumber = StdIn.readLine().toIntOption
    if (cardNumber.isEmpty) {
      println("Invalid input. Please enter a number.")
      gameContinue()
    } else {
      processInputLine(cardNumber.get, currentPlayer.hand.cards.length)
    }
  }

  private def undoAction(): Unit = {
    controller.undo()
    gameContinue()
  }

  private def redoAction(): Unit = {
    controller.redo()
    gameContinue()
  }
}