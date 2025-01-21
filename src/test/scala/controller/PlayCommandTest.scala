package controller

import model.*
import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import controller.command.commandIm.PlayCommand
import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.{IPlayer, IPlayerHand}
import model.cardComponent.ICard
import model.gameComponent.gameIm.UnoField
//import controller.command.commandIm.PlayCommand
import controller.controllerComponent.IUnoController
import scala.util.{Failure, Success}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*
import model.fileIoComponent.IFileIo
import util.Event
import model.gameComponent.IUnoField


class PlayCommandTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = mock(classOf[ICard])
    UnoField(players, topCard, 0)
  }

  /*test("execute() should play a valid card and update the game state") { // failed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))

    //val topCard = mock(classOf[ICard])
    //val validCard = Card(cardColors.RED, cardValues.THREE)
    val validCard = mock(classOf[ICard])
    //val topCard = mock(classOf[ICard])
    //when(validCard.canBePlayed(topCard)).thenReturn(true)

    doReturn(cardColors.RED).when(initialField.topCard).getColor
    doReturn(cardValues.TWO).when(initialField.topCard).getValue
    doReturn(cardColors.RED).when(validCard).getColor
    doReturn(cardValues.THREE).when(validCard).getValue
    doReturn(true).when(validCard).canBePlayed(initialField.topCard)

    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.cards.contains(validCard) shouldBe false
  }*/

  test("execute() should fail for an invalid card") { // failed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    //val controller = spy(new UnoController(initialField, mockFileIo))
    val controller = new UnoController(initialField, mockFileIo)
    //val invalidCard = Card(cardColors.RED, cardValues.THREE)
    val invalidCard = mock(classOf[ICard])
    val playerHand = mock(classOf[IPlayerHand])
    when(controller.field.topCard).thenReturn(initialField.topCard)
    when(invalidCard.canBePlayed(controller.field.topCard)).thenReturn(false)

    val currentPlayer = controller.field.players(initialField.currentPlayer)
    when(currentPlayer.hand).thenReturn(playerHand) // Correctly mock IPlayer's hand

    //doReturn(cardColors.RED).when(invalidCard).getColor
    //doReturn(cardValues.THREE).when(invalidCard).getValue
    val playCommand = new PlayCommand(controller, invalidCard)
    val result = playCommand.doStep(playCommand)

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalArgumentException]
  }

  test("undo() should revert the game state to before the card was played") { // passed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    val result = playCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField // The state should revert to the initial field
  }

  test("redo() should reapply the playing of the card") { // failed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    //val validCard = Card(cardColors.RED, cardValues.THREE)
    val validCard = mock(classOf[ICard])
    val playCommand = new PlayCommand(controller, validCard)

    //val topCard = mock(classOf[ICard])
    doReturn(cardColors.RED).when(initialField.topCard).getColor
    doReturn(cardValues.TWO).when(initialField.topCard).getValue
    doReturn(true).when(validCard).canBePlayed(initialField.topCard)

    playCommand.doStep(playCommand) // Perform the play
    playCommand.undoStep() // Undo the play
    when(initialField.topCard.getColor).thenReturn(cardColors.RED)
    when(initialField.topCard.getValue).thenReturn(cardValues.TWO)

    val result = playCommand.redoStep() // Redo the play

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    controller.field.players(initialField.currentPlayer).hand.cards.contains(validCard) shouldBe false
  }

  test("undo() without a prior execute should fail") { // passed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() without a prior execute should fail") { // passed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }

  test("redo() should fail if the card cannot be replayed") { // passed
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = Card(cardColors.RED, cardValues.THREE)
    val playCommand = new PlayCommand(controller, validCard)

    playCommand.doStep(playCommand) // Perform the play
    playCommand.undoStep() // Undo the play
    // Modify the game state so the card can no longer be played
    controller.field = controller.field.copy(
      players = initialField.players, // Retain the current players
      topCard = Card(cardColors.BLUE, cardValues.FIVE), // Set a new top card
      currentPlayer = initialField.currentPlayer // Retain the current player
    )
    val result = playCommand.redoStep() // Try to redo the play

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }
}