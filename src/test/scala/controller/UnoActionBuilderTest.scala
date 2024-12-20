package controller

import model.*
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class UnoActionBuilderTest extends AnyFunSuite with Matchers {

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
    val controller = new UnoController(new UnoField())
    val player = new Player(0, PlayerHand(List(card)))
    controller.field = controller.field.copy(players = List(player))
    val action = new UnoActionBuilder.PlayAction(card)

    action.executeAction(controller, player)

    controller.field.topCard shouldBe card
    controller.field.players(controller.field.currentPlayer).hand.cards shouldBe empty
  }

  test("DrawAction.executeAction() should call draw on the controller") {
    val controller = new UnoController(new UnoField())
    val player = new Player(0, PlayerHand(List()))
    val action = new UnoActionBuilder.DrawAction

    action.executeAction(controller, player)

    controller.field.players(controller.field.currentPlayer).hand.cards.size shouldBe 1
  }
}