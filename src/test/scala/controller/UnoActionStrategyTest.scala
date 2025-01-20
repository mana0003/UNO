package controller

import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.{DrawCardStrategy, PlayCardStrategy, UnoActionHandler}
import model.*
import model.cardComponent.cardIm.Card
import model.gameComponent.IPlayer
import model.cardComponent.ICard
import model.cardComponent.{cardColors, cardValues}
import model.fileIoComponent.IFileIo
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*

class UnoActionStrategyTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = mock(classOf[ICard])
    new UnoField(players, topCard, 0)
  }

  test("DrawCardStrategy should invoke draw on the controller") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val player = new Player(0, PlayerHand(List()))

    val strategy = new DrawCardStrategy
    strategy.executeAction(controller, player)

    // Verify that the player's hand has one more card
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }

  test("PlayCardStrategy should invoke play on the controller with the specified card") {
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mock(classOf[IFileIo])))
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(
      players = List(player),
      topCard = initialField.topCard,
      currentPlayer = initialField.currentPlayer
    )
    val strategy = new PlayCardStrategy(card)
    strategy.executeAction(controller, player)

    // Verify that the card is played and the player's hand is empty
    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("UnoActionHandler should execute the assigned DrawCardStrategy") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val player = new Player(0, PlayerHand(List()))

    val drawStrategy = new DrawCardStrategy
    val actionHandler = new UnoActionHandler(drawStrategy)

    actionHandler.executeStrategy(controller, player)

    // Verify that the player's hand has one more card
    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }

  test("UnoActionHandler should execute the assigned PlayCardStrategy") {
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mock(classOf[IFileIo])))
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(
      players = List(player),
      topCard = initialField.topCard,
      currentPlayer = initialField.currentPlayer
    )
    val playStrategy = new PlayCardStrategy(card)
    val actionHandler = new UnoActionHandler(playStrategy)

    actionHandler.executeStrategy(controller, player)

    // Verify that the card is played and the player's hand is empty
    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("UnoActionHandler should allow strategy swapping at runtime") {
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mock(classOf[IFileIo])))
    val card = Card(cardColors.RED, cardValues.THREE)
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(
      players = List(player),
      topCard = initialField.topCard,
      currentPlayer = initialField.currentPlayer
    )
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