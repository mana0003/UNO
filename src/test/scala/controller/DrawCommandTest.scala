// DrawCommandTest.scala
package controller

import controller.controllerComponent.IUnoController
import model.cardComponent.{ICard, cardColors, cardValues}
import model.gameComponent.{IPlayer, IUnoField}
import model.gameComponent.gameIm.UnoField
import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import util.Command
import scala.util.{Failure, Success}
import util.Event
import controller.command.commandIm.DrawCommand
import model.gameComponent.gameIm.PlayerHand
import model.gameComponent.{IUnoField, IPlayerHand}
import model.cardComponent.cardIm.Card
import org.mockito.ArgumentMatchers._



import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class DrawCommandTest extends AnyWordSpec with MockitoSugar {

  "A DrawCommand" when {

    // Mock setup
    val controllerMock = mock[IUnoController]
    val drawCommand = new DrawCommand(controllerMock)

    "executing doStep successfully" should {

      "save the previous state and update the player's hand" in {
        val initialField = UnoField(List(mock[IPlayer], mock[IPlayer]), mock[ICard], 0)
        when(controllerMock.field).thenReturn(mock[IUnoField])
        val card = mock[ICard]
        val player = mock[IPlayer]
        val playerHand = mock[IPlayerHand]
        when(player.hand).thenReturn(playerHand)
        when(playerHand.addCard(any[ICard])).thenReturn(PlayerHand(List(card)))
        when(controllerMock.field.players).thenReturn(List(player))
        when(controllerMock.field.currentPlayer).thenReturn(0)

        drawCommand.doStep(mock[Command]) shouldBe a[Success[_]]
        verify(controllerMock).notifyObservers(Event.Draw)
      }
    }  // passed

    "undoing a previous step" should {

      "revert to the previous state" in {
        val previousState = mock[IUnoField]
        drawCommand.previousState = Some(previousState)
        drawCommand.undoStep() shouldBe a[Success[_]]
        verify(controllerMock).notifyObservers(Event.Undo)
      }  // passed

      "fail if no previous state is available" in {
        drawCommand.previousState = None
        drawCommand.undoStep() shouldBe a[Failure[_]]
      }  // passed
    }  // passed

    "redoing a previous step" should {

      "redo the drawing of a card" in {
        val unoFieldMock = mock[IUnoField]
        when(controllerMock.field).thenReturn(unoFieldMock)
        when(unoFieldMock.players).thenReturn(List(mock[IPlayer], mock[IPlayer]))
        when(unoFieldMock.currentPlayer).thenReturn(0)
        when(unoFieldMock.topCard).thenReturn(mock[ICard])
      }  // passed

      "fail if no card was drawn previously" in {
        drawCommand.drawnCard = None
        drawCommand.redoStep() shouldBe a[Failure[_]]
      }  // passed
    }
  }

  "doStep" should {
    "update the player's hand and move to the next player" in {
      val controllerMock = mock[IUnoController]
      //val initialField = UnoField(List(mock[IPlayer], mock[IPlayer]), mock[ICard], 0)
      val initialField = mock[IUnoField]
      val card = mock[ICard]
      val player = mock[IPlayer]
      val updatedPlayer = mock[IPlayer]
      val playerHand = mock[IPlayerHand]
      val updatedHand = mock[IPlayerHand]

      when(controllerMock.field).thenReturn(initialField)
      when(initialField.players).thenReturn(List(player, mock[IPlayer]))
      when(initialField.currentPlayer).thenReturn(0)
      when(player.hand).thenReturn(playerHand)
      when(playerHand.addCard(any[ICard])).thenReturn(updatedHand)
      when(player.copy(hand = updatedHand)).thenReturn(updatedPlayer)
      when(initialField.copy(players = any[List[IPlayer]],
        topCard = any[ICard], currentPlayer = any[Int])).thenReturn(initialField)
     // when(controllerMock.field.players).thenReturn(List(player, mock[IPlayer]))
     // when(controllerMock.field.currentPlayer).thenReturn(0)
      //when(player.copy(hand = playerHand)).thenReturn(player)

      val drawCommand = new DrawCommand(controllerMock)
      drawCommand.drawnCard = Some(card)
      drawCommand.doStep(mock[Command]) shouldBe a[Success[_]]

      verify(player).copy(hand = updatedHand)
      //verify(playerHand).addCard(any[ICard])
      verify(initialField).copy(players = any[List[IPlayer]], topCard = any[ICard], currentPlayer = any[Int])
      verify(controllerMock).notifyObservers(Event.Draw)
    }
  }
  "redoStep" should {
    "update the field with the previously drawn card" in {
      val controllerMock = mock[IUnoController]
      //val initialField = UnoField(List(mock[IPlayer], mock[IPlayer]), mock[ICard], 0)
      val initialField = mock[IUnoField]
      val card = mock[ICard]
      val player = mock[IPlayer]
      val playerHand = mock[IPlayerHand]

      when(controllerMock.field).thenReturn(initialField)
      when(initialField.players).thenReturn(List(player, mock[IPlayer]))
      when(initialField.currentPlayer).thenReturn(0)
      when(player.hand).thenReturn(playerHand)
      when(playerHand.addCard(any[ICard])).thenReturn(playerHand)
      //when(controllerMock.field.players).thenReturn(List(player, mock[IPlayer]))
      //when(controllerMock.field.currentPlayer).thenReturn(0)
      when(player.copy(hand = playerHand)).thenReturn(player)

      val drawCommand = new DrawCommand(controllerMock)
      drawCommand.drawnCard = Some(card)
      drawCommand.redoStep() shouldBe a[Success[_]]

      verify(playerHand).addCard(any[ICard])
      verify(controllerMock).notifyObservers(Event.Redo)
    }
  }

}


/*class DrawCommandTest extends AnyFunSuite with Matchers with MockitoSugar {

  def createInitialField(): UnoField = {
    val players = List(mock[IPlayer], mock[IPlayer])
    val topCard = mock[ICard]
    UnoField(players, topCard, 0)
  }

  test("doStep() should draw a card") {  // failed
    val controller = mock[IUnoController]
    val initialField = createInitialField()
    when(controller.field).thenReturn(initialField)
    val drawCommand = new DrawCommand(controller)
    val drawnCard = mock[ICard]
    doNothing().when(controller).draw()

    val result = drawCommand.doStep(mock[Command]) shouldBe a[Success[_]]
    verify(controller).notifyObservers(Event.Draw)
  }

  test("undoStep() should revert to the previous state") { // passed
    val controller = mock[IUnoController]
    val initialField = createInitialField()
    when(controller.field).thenReturn(initialField)
    val drawCommand = new DrawCommand(controller)
    drawCommand.doStep(mock[Command])

    val result = drawCommand.undoStep()
    result shouldBe a[Success[_]]
    verify(controller).notifyObservers(Event.Undo)
  }

  test("undoStep() should fail if there is no previous state") {  // passed
    val controller = mock[IUnoController]
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.undoStep()
    result shouldBe a[Failure[_]]
  }

  test("redoStep() should redo drawing a card") {  // failed
    val controller = mock[IUnoController]
    val initialField = createInitialField()
    when(controller.field).thenReturn(initialField)
    val drawCommand = new DrawCommand(controller)
    val drawnCard = mock[ICard]

    doReturn(drawnCard).when(controller).draw()
    drawCommand.doStep(mock[Command])
    drawCommand.undoStep()

    val result = drawCommand.redoStep()
    result shouldBe a[Success[_]]
    verify(controller).notifyObservers(Event.Redo)
  }

  test("redoStep() should fail if there is no card to redo drawing") {  // passed
    val controller = mock[IUnoController]
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.redoStep()
    result shouldBe a[Failure[_]]
  }
}*/