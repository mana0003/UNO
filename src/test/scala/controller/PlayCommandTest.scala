package controller

import model.*
import controller.*
import model.cardComponent.cardIm.{Card, cardColors, cardValues}
import model.gameComponent.gameIm.UnoField
//import controller.command.commandIm.PlayCommand
//import controller.controllerComponent.ControllerIm.UnoController
import util.*

import scala.util.{Failure, Success}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class PlayCommandTest extends AnyFunSuite with Matchers {

  test("execute() should play a valid card and update the game state") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.cards.contains(validCard) shouldBe false
  }

  test("execute() should fail for an invalid card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val invalidCard = Card(cardColors.RED, cardValues.THREE) // Assuming this card is invalid in the current context
    val playCommand = new PlayCommand(controller, invalidCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalArgumentException]
  }

  test("undo() should revert the game state to before the card was played") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    val result = playCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField // The state should revert to the initial field
  }

  test("redo() should reapply the playing of the card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    playCommand.undoStep() // Undo the play
    val result = playCommand.redoStep() // Redo the play

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.cards.contains(validCard) shouldBe false
  }

  test("undo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() should fail if the card cannot be replayed") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    playCommand.undoStep() // Undo the play
    // Modify the game state so the card can no longer be played
    controller.field = controller.field.copy(topCard = Card(cardColors.BLUE, cardValues.FIVE))
    val result = playCommand.redoStep() // Try to redo the play

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }
}