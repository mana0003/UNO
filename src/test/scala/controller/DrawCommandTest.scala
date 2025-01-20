package controller

import controller._
import controller.command.commandIm.DrawCommand
import controller.controllerComponent.ControllerIm.UnoController
import model._
import model.gameComponent.gameIm.UnoField
import model.gameComponent.IPlayer
import model.cardComponent.ICard
import util._
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import model.fileIoComponent.IFileIo

import scala.util.{Failure, Success}

class DrawCommandTest extends AnyFunSuite with Matchers {

  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = mock(classOf[ICard])
    new UnoField(players, topCard, 0)
  }

  test("execute() should draw a card and update the player's hand") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))    
    val drawCommand = new DrawCommand(controller)
    val drawnCard = mock(classOf[ICard])

    //when(controller.getField).thenReturn(initialField)
    doReturn(mock(classOf[ICard])).when(controller).draw()
    //doReturn(initialField).when(controller).getField
    doReturn(drawnCard).when(controller).draw()
    val result = drawCommand.doStep(drawCommand)
    result shouldBe a[Success[_]]

    val currentPlayer = controller.field.players(controller.field.currentPlayer)
    currentPlayer.hand.cards.size shouldBe (initialField.players(initialField.currentPlayer).hand.cards.size + 1)
  }

  test("undo() should revert to the previous state") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val drawCommand = new DrawCommand(controller)

    drawCommand.doStep(drawCommand) // Perform a draw
    val result = drawCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField // The state should revert to the initial field
  }

  test("redo() should reapply the drawing of the card") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val drawCommand = new DrawCommand(controller)

    doReturn(mock(classOf[ICard])).when(controller).draw() // Properly stub draw()

    drawCommand.doStep(drawCommand) // Perform a draw
    drawCommand.undoStep() // Undo the draw
    val result = drawCommand.redoStep() // Redo the draw

    result shouldBe a[Success[_]]
    val updatedPlayer = controller.field.players(controller.field.currentPlayer)
    updatedPlayer.hand.cards.size shouldBe (initialField.players(initialField.currentPlayer).hand.cards.size + 1)
  }

  test("undo() without a prior execute should fail") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[IllegalStateException]
  }

  test("redo() without a prior execute should fail") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[IllegalStateException]
  }
}
