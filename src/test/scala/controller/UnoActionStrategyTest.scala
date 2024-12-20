package controller

import model.*
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class UnoActionStrategyTest extends AnyFunSuite with Matchers {

  test("DrawCardStrategy should invoke draw on the controller") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List()))

    val strategy = new DrawCardStrategy
    strategy.executeAction(controller, player)

    // Verify that the player's hand has one more card
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }

  test("PlayCardStrategy should invoke play on the controller with the specified card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(players = List(player))

    val strategy = new PlayCardStrategy(card)
    strategy.executeAction(controller, player)

    // Verify that the card is played and the player's hand is empty
    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("UnoActionHandler should execute the assigned DrawCardStrategy") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val player = new Player(0, PlayerHand(List()))

    val drawStrategy = new DrawCardStrategy
    val actionHandler = new UnoActionHandler(drawStrategy)

    actionHandler.executeStrategy(controller, player)

    // Verify that the player's hand has one more card
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }

  test("UnoActionHandler should execute the assigned PlayCardStrategy") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(players = List(player))

    val playStrategy = new PlayCardStrategy(card)
    val actionHandler = new UnoActionHandler(playStrategy)

    actionHandler.executeStrategy(controller, player)

    // Verify that the card is played and the player's hand is empty
    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("UnoActionHandler should allow strategy swapping at runtime") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(players = List(player))

    val drawStrategy = new DrawCardStrategy
    val playStrategy = new PlayCardStrategy(card)

    val actionHandler = new UnoActionHandler(drawStrategy)

    // Initially set to DrawCardStrategy
    actionHandler.executeStrategy(controller, player)
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 2

    // Swap to PlayCardStrategy
    actionHandler.setStrategy(playStrategy)
    actionHandler.executeStrategy(controller, player)
    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }
}