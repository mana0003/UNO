package controller

import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.patternsIm.ConcreteUnoActionProcessor
import model.*
import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.IPlayer
import model.cardComponent.ICard
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import util.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class UnoActionProcessorTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(
      new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE)))),
      new Player(1, PlayerHand(List(Card(cardColors.BLUE, cardValues.TWO))))
    )
    val topCard = Card(cardColors.RED, cardValues.THREE)
    new UnoField(players, topCard, 0)
  }

  test("processAction should successfully process a 'play' action for a valid card") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = controller.field.players(controller.field.currentPlayer)

    val cardToPlay = player.hand.cards.head
    controller.field = controller.field.copy(
      players = controller.field.players,
      topCard = Card(cardColors.RED, cardValues.TWO),
      currentPlayer = controller.field.currentPlayer
    )

    processor.processAction(controller, player, "play")

    controller.field.topCard should equal(cardToPlay)
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("processAction should throw an exception for an invalid 'play' action") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = new Player(0, PlayerHand(List(Card(cardColors.BLUE, cardValues.THREE))))

    val topCard = Card(cardColors.RED, cardValues.TWO)
    controller.field = controller.field.copy(
      players = controller.field.players,
      topCard = topCard,
      currentPlayer = controller.field.currentPlayer
    )

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "play")
    }
  }

  test("processAction should throw an exception for an unknown action type") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = controller.field.players(controller.field.currentPlayer)

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "unknown")
    }
  }

  test("handleAction should invoke the correct handler from UnoActionBuilder") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = controller.field.players(controller.field.currentPlayer)

    processor.handleAction(controller, player, "play")

    controller.field.topCard should equal(player.hand.cards.head)
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("completeAction should notify observers correctly for 'draw' and 'play' actions") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = controller.field.players(controller.field.currentPlayer)

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "play")
    }

    controller.notifyObservers(Event.Play)

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "draw")
    }

    controller.notifyObservers(Event.Draw)
  }

  test("completeAction should throw an exception for an unknown action type") {
    val controller = new UnoController(createInitialField())
    val processor = new ConcreteUnoActionProcessor
    val player = controller.field.players(controller.field.currentPlayer)

    an[IllegalArgumentException] should be thrownBy {
      processor.completeAction(controller, player, "unknown")
    }
  }
}
