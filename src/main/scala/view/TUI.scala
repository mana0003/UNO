package scala.view

import scala.model.*
import scala.controller.*
import scala.util.*
import scala.io.StdIn
import scala.io.AnsiColor._
import scala.util.{Event, Observer}
import scala.util.Event.{Quit, Start}

class TUI(val controller: UnoController) extends Observer {
  controller.add(this)

  private def processInputLine(input: Int, handSize: Int): Unit = {
    if (input < 1 || input > handSize) {
      println("Invalid card number. Please try again.")
      gameContinue()
    } else {
      val card = controller.field.players(controller.field.currentPlayer).hand.cards(input - 1)
      if (controller.field.players(controller.field.currentPlayer).valid(card) && card.canBePlayedOn(controller.field.topCard)) {
        UnoActionFactory.getActionHandler("play", Some(card)).executeAction(controller, controller.field.players(controller.field.currentPlayer))
      } else {
        println("Card does not fit. Do you want to:")
        println("1. Draw a card")
        println("2. Try again")
        println("3. Quit")
        val readLine = StdIn.readLine().toIntOption.getOrElse(-1)
        readLine match {
          case 1 => UnoActionFactory.getActionHandler("draw").executeAction(controller, controller.field.players(controller.field.currentPlayer))
          case 2 => gameContinue()
          case 3 => controller.notifyObservers(Event.Quit)
          case _ =>
            println("Invalid input. Please enter a number.")
            gameContinue()
        }
      }
    }
  }

  override def update(e: Event): Unit = {
    e match {
      case Quit =>
        if (!controller.field.players.exists(_.hand.cards.isEmpty)) {
          // no winner yet
        } else {
          gameOver()
        }
      case _ => gameContinue()
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
    val winnerIndex = controller.field.players.indexWhere(_.hand.cards.isEmpty)
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

  private def gameContinue(): Unit = {
    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    println(s"Current player: Player ${controller.field.currentPlayer + 1}")
    println(s"Current top card: ${controller.field.topCard.getColorCode}${controller.field.topCard.value}$RESET")
    currentPlayer.hand.cards.zipWithIndex.foreach { case (card, index) =>
      println(s"${index + 1}: ${card.getColorCode}${card.value}$RESET")
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
}