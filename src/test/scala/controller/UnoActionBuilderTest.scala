package controller

import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.patternsIm.UnoActionBuilder
import model.*
import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.gameComponent.IPlayer
import model.cardComponent.ICard
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*

class UnoActionBuilderTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = mock(classOf[ICard])
    new UnoField(players, topCard, 0)
  }
  
  test("builder() should create a new UnoActionBuilder instance") {
    val builder = UnoActionBuilder.builder()
    builder shouldBe a[UnoActionBuilder.UnoActionBuilder]
  }

  test("setAction() should set the action type in the builder") {
    val builder = UnoActionBuilder.builder()
    builder.setAction("play") shouldBe theSameInstanceAs(builder)
  }

  test("setCard() should set the card in the builder") {
    val card = Card(cardColors.RED, cardValues.THREE)
    val builder = UnoActionBuilder.builder()
    builder.setCard(card) shouldBe theSameInstanceAs(builder)
  }

  test("build() should create a PlayAction for action 'play'") {
    val card = Card(cardColors.RED, cardValues.THREE)
    val builder = UnoActionBuilder.builder()
      .setAction("play")
      .setCard(card)

    val action = builder.build()
    action shouldBe a[UnoActionBuilder.PlayAction]
  }

  test("build() should create a DrawAction for action 'draw'") {
    val builder = UnoActionBuilder.builder()
      .setAction("draw")

    val action = builder.build()
    action shouldBe a[UnoActionBuilder.DrawAction]
  }

  test("build() should throw IllegalArgumentException for unknown action") {
    val builder = UnoActionBuilder.builder()
      .setAction("unknown")

    intercept[IllegalArgumentException] {
      builder.build()
    }
  }

  test("PlayAction.executeAction() should call play on the controller") {
    val card = Card(cardColors.RED, cardValues.THREE)
    val controller = new UnoController(createInitialField())
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(players = List(player))
    val action = new UnoActionBuilder.PlayAction(card)

    action.executeAction(controller, player)

    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("DrawAction.executeAction() should call draw on the controller") {
    val controller = new UnoController(createInitialField())
    val player = new Player(0, PlayerHand(List()))
    val action = new UnoActionBuilder.DrawAction

    action.executeAction(controller, player)

    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }
}