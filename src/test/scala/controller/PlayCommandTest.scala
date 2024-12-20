package controller

import model.*
import controller.*
import util.*
import scala.util.{Success, Failure}
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._


class PlayCommandTest extends AnyFunSuite with Matchers {

  test("execute() should play a valid card and update the game state") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.contains(validCard) shouldBe false
  }

  test("execute() should fail for an invalid card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val invalidCard = mock[Card]
    val playCommand = new PlayCommand(controller, invalidCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalArgumentException]
  }

  test("undo() should revert the game state to before the card was played") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    val result = playCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField // The state should revert to the initial field
  }

  test("redo() should reapply the playing of the card") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    playCommand.undoStep() // Undo the play
    val result = PlayCommand.redoStep() // Redo the play

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.contains(validCard) shouldBe false
  }

  test("undo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    val result = PlayCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() without a prior execute should fail") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    val result = PlayCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() should fail if the card cannot be replayed") {
    val initialField = new UnoField()
    val controller = new UnoController(initialField)
    val validCard = mock[Card]
    val playCommand = new PlayCommand(controller, validCard)

    PlayCommand.doStep(playCommand) // Perform the play
    PlayCommand.undoStep() // Undo the play
    controller.field = // Modify the game state so the card can no longer be played
    val result = PlayCommand.redoStep() // Try to redo the play

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }
}
