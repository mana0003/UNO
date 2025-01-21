package controller

import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.UnoActionBuilder
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
import model.fileIoComponent.IFileIo


class UnoActionBuilderTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = Card(cardColors.RED, cardValues.THREE)
    UnoField(players, topCard, 0)
  }

  test("builder() should create a new UnoActionBuilder instance") {
    val builder = UnoActionBuilder.builder()
    builder shouldBe a[UnoActionBuilder.UnoActionBuilder]
  } // passed

  test("setAction() should set the action type in the builder") {
    val builder = UnoActionBuilder.builder()
    builder.setAction("play") shouldBe theSameInstanceAs(builder)
  } // passed

  test("setCard() should set the card in the builder") {
    val card = Card(cardColors.RED, cardValues.THREE)
    val builder = UnoActionBuilder.builder()
    builder.setCard(card) shouldBe theSameInstanceAs(builder)
  } // passed

  test("build() should create a PlayAction for action 'play'") {
    val card = Card(cardColors.RED, cardValues.THREE)
    val builder = UnoActionBuilder.builder()
      .setAction("play")
      .setCard(card)

    val action = builder.build()
    action shouldBe a[UnoActionBuilder.PlayAction]
  } // passed

  test("build() should create a DrawAction for action 'draw'") {
    val builder = UnoActionBuilder.builder()
      .setAction("draw")

    val action = builder.build()
    action shouldBe a[UnoActionBuilder.DrawAction]
  } // passed

  test("build() should throw IllegalArgumentException for unknown action") {
    val builder = UnoActionBuilder.builder()
      .setAction("unknown")

    intercept[IllegalArgumentException] {
      builder.build()
    }
  } // passed

  test("PlayAction.executeAction() should call play on the controller") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val player = mock(classOf[Player])
    val playerHand = mock(classOf[PlayerHand])
    val card = Card(cardColors.RED, cardValues.THREE)
    when(player.hand).thenReturn(playerHand)
    when(playerHand.cards).thenReturn(List(card))

    val action = new UnoActionBuilder.PlayAction(card)

    action.executeAction(controller, player)
    verify(controller).play(card)
  }  // passed

  test("DrawAction.executeAction() should call draw on the controller") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val player = mock(classOf[Player])
    val action = new UnoActionBuilder.DrawAction
    action.executeAction(controller, player)
    verify(controller).draw()
  }  // passed
}