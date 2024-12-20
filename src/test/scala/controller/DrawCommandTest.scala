package controller

import controller.*
import model.*
import util.*
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import scala.util.{Success, Failure}

class DrawCommandTest extends AnyFunSuite with Matchers {

  test("execute() should draw a card and update the player's hand") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.doStep(drawCommand)

    result shouldBe a[Success[_]]
    val updatedPlayer = controller.field.players(controller.field.currentPlayer)
    updatedPlayer.hand.cards.size shouldBe (initialField.players(initialField.currentPlayer).hand.cards.size + 1)
  }

  test("undo() should revert to the previous state") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val drawCommand = new DrawCommand(controller)

    drawCommand.doStep(drawCommand) // Perform a draw
    val result = drawCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField // The state should revert to the initial field
  }

  test("redo() should reapply the drawing of the card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val drawCommand = new DrawCommand(controller)

    drawCommand.doStep(drawCommand) // Perform a draw
    drawCommand.undoStep() // Undo the draw
    val result = drawCommand.redoStep() // Redo the draw

    result shouldBe a[Success[_]]
    val updatedPlayer = controller.field.players(controller.field.currentPlayer)
    updatedPlayer.hand.cards.size shouldBe (initialField.players(initialField.currentPlayer).hand.cards.size + 1)
  }

  test("undo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[IllegalStateException]
  }

  test("redo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val drawCommand = new DrawCommand(controller)

    val result = drawCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[IllegalStateException]
  }
}
