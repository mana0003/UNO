package controller

import controller.*
import model.*
import util.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class UnoActionProcessorTest extends AnyFunSuite with Matchers {

  test("processAction should successfully process a 'play' action for a valid card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.THREE))))
    val topCard = Card(cardColors.RED, cardValues.TWO)
    controller.field = controller.field.copy(topCard = topCard)

    val processor = new ConcreteUnoActionProcessor

    noException shouldBe thrownBy {
      processor.processAction(controller, player, "play")
    }

    controller.field.topCard shouldBe player.hand.cards.head
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("processAction should throw an exception for an invalid 'play' action") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.BLUE, cardValues.THREE))))
    val topCard = Card(cardColors.RED, cardValues.TWO)
    controller.field = controller.field.copy(topCard = topCard)

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "play")
    }
  }

  test("processAction should throw an exception for an unknown action type") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.THREE))))

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "unknown")
    }
  }

  test("handleAction should invoke the correct handler from UnoActionBuilder") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.THREE))))
    val action = "play"

    val processor = new ConcreteUnoActionProcessor

    processor.handleAction(controller, player, action)

    controller.field.topCard shouldBe player.hand.cards.head
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("completeAction should notify observers correctly for 'draw' and 'play' actions") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.THREE))))

    val processor = new ConcreteUnoActionProcessor

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "play")
    }

    controller.field.topCard shouldBe player.hand.cards.head

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "draw")
    }

    // Assuming draw action adds a card to the player's hand
    controller.field.players(controller.field.currentPlayer).hand.cards should not be empty
  }

  test("completeAction should throw an exception for an unknown action type") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.THREE))))

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.completeAction(controller, player, "unknown")
    }
  }
}