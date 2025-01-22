package controller.command.commandIm
import model.fileIoComponent.IFileIo
import controller.*
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import util.*
import model.cardComponent.{ICard, cardColors, cardValues}
import util.Event.{Play, Undo, Win, Redo}
import scala.util.{Failure, Success, Try}
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import model.gameComponent.gameIm.UnoField
import controller.controllerComponent.ControllerIm.UnoController

class PlayCommandTest extends AnyFunSuite with Matchers {
  def createInitialField(): IUnoField = {
    val playerHand1 = mock(classOf[IPlayerHand])
    val playerHand2 = mock(classOf[IPlayerHand])
    val player1 = mock(classOf[IPlayer])
    val player2 = mock(classOf[IPlayer])
    when(player1.hand).thenReturn(playerHand1)
    when(player2.hand).thenReturn(playerHand2)
    val players = List(player1, player2)
    val topCard = mock(classOf[ICard])
    when(topCard.getColor).thenReturn(cardColors.RED)
    when(topCard.getValue).thenReturn(cardValues.TWO)
    UnoField(players, topCard, currentPlayer = 0)
  }

  test("doStep should play a valid card and update the game state") {
  val mockFileIo = mock(classOf[IFileIo])
  val initialField = createInitialField()
  val controller = spy(new UnoController(initialField, mockFileIo))

  val validCard = mock(classOf[ICard])
  when(validCard.canBePlayed(initialField.topCard)).thenReturn(true)
  when(validCard.getColor).thenReturn(cardColors.RED)
  when(validCard.getValue).thenReturn(cardValues.THREE)

  val updatedHand = mock(classOf[IPlayerHand])
  when(initialField.players.head.hand.removeCard(validCard)).thenReturn(updatedHand)

  val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.doStep(playCommand)

    result match {
  case Success(_) =>
    val updatedCurrentPlayer = controller.field.players(controller.field.currentPlayer)
    updatedCurrentPlayer.hand should not be null
    controller.field.topCard shouldBe validCard
    verify(controller, times(1)).notifyObservers(Play)
  case Failure(exception) => fail(s"Unexpected failure: $exception")
}

  result shouldBe a[Success[_]]
  controller.field.topCard shouldBe validCard
  verify(controller, times(1)).notifyObservers(Play)
}

  test("doStep should throw an exception for an invalid card") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))

    val invalidCard = mock(classOf[ICard])
    when(invalidCard.canBePlayed(initialField.topCard)).thenReturn(false)

    val playCommand = new PlayCommand(controller, invalidCard)

    val result = playCommand.doStep(playCommand)

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalArgumentException]
  }  // passed

  test("undoStep should revert the game state to before the card was played") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = mock(classOf[ICard])
    when(validCard.canBePlayed(initialField.topCard)).thenReturn(true)

    val playCommand = new PlayCommand(controller, validCard)
    playCommand.doStep(playCommand)

    val result = playCommand.undoStep()

    result shouldBe a[Success[_]]
    controller.field shouldBe initialField
    verify(controller, times(1)).notifyObservers(Undo)
  }  // passed

  test("undoStep should fail if there is no previous state") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = mock(classOf[ICard])

    val playCommand = new PlayCommand(controller, validCard)

    val result = playCommand.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }  // passed

  test("redoStep should reapply the playing of the card") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val validCard = mock(classOf[ICard])
    when(validCard.canBePlayed(initialField.topCard)).thenReturn(true)

    val playCommand = new PlayCommand(controller, validCard)
    playCommand.doStep(playCommand)
    playCommand.undoStep()

    val result = playCommand.redoStep()

    result shouldBe a[Success[_]]
    controller.field.topCard shouldBe validCard
    verify(controller, times(1)).notifyObservers(Redo)
  }  // passed

  test("redoStep should fail if the card cannot be replayed") {
    val mockFileIo = mock(classOf[IFileIo])
    val initialField = createInitialField()
    val controller = spy(new UnoController(initialField, mockFileIo))
    val invalidCard = mock(classOf[ICard])
    when(invalidCard.canBePlayed(initialField.topCard)).thenReturn(false)

    val playCommand = new PlayCommand(controller, invalidCard)

    val result = playCommand.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe an[IllegalStateException]
  }  // passed
}