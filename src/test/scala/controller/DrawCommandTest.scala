// DrawCommandTest.scala
package controller

import controller.controllerComponent.IUnoController
import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.gameComponent.gameIm.UnoField
import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import util.Command
import scala.util.{Failure, Success}
import util.{Event}
import controller.command.commandIm.DrawCommand
import model.gameComponent.gameIm.PlayerHand

class DrawCommandTest extends AnyFunSuite with Matchers with MockitoSugar {

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
}