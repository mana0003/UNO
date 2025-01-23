package controller

import controller.*
import controller.controllerComponent.IUnoController
import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.UnoActionBuilder.UnoActionBuilder
import controller.patterns.UnoActionBuilder.UnoAction
import controller.patterns.UnoActionStrategy
import controller.patterns.*
import model.*
import model.cardComponent.cardIm.Card
import model.cardComponent.cardValues.SEVEN
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import model.cardComponent.{ICard, cardColors, cardValues}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import util.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers._
import model.fileIoComponent.IFileIo
import org.scalatest.flatspec.AnyFlatSpec


class UnoActionProcessorTest extends AnyFunSuite with Matchers {
  def createInitialField(): UnoField = {
    val players = List(
      new Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE)))),
      new Player(1, PlayerHand(List(Card(cardColors.BLUE, cardValues.TWO))))
    )
    val topCard = Card(cardColors.RED, cardValues.THREE)
    new UnoField(players, topCard, 0)
  }

  test("processAction should successfully process a 'play' action for a valid card") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val actionBuilder = mock(classOf[UnoActionBuilder.UnoActionBuilder])
    val player = controller.field.players(controller.field.currentPlayer)

    val cardToPlay = new Card(cardColors.RED, cardValues.FIVE)
    val topCard = new Card(cardColors.RED, cardValues.SEVEN)

    when(actionBuilder.setAction("play")).thenReturn(actionBuilder)
    when(actionBuilder.setCard(cardToPlay)).thenReturn(actionBuilder)
    when(actionBuilder.build()).thenReturn(new UnoActionBuilder.PlayAction(cardToPlay))

    val processor = new ConcreteUnoActionProcessor(UnoActionBuilder.builder()) {
      override def handleAction(controller: UnoController, player: IPlayer, action: String): Unit = {
        actionBuilder.setAction(action).setCard(cardToPlay).build().executeAction(controller, player)
      }
    }
    controller.field = controller.field.copy(
      players = controller.field.players,
      topCard = topCard,
      currentPlayer = controller.field.currentPlayer
    )

    println(s"Player's hand before play: ${player.hand.cards}")
    processor.processAction(controller, player, "play")
    val updatedPlayer = controller.field.players(controller.field.currentPlayer)
    println(s"Player's hand after play: ${updatedPlayer.hand.cards}")

    controller.field.topCard should equal(cardToPlay)
  }

  test("processAction should throw an exception for an invalid 'play' action") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val actionBuilder = mock(classOf[UnoActionBuilder])
    val processor = new ConcreteUnoActionProcessor(actionBuilder)
    val player = new Player(0, PlayerHand(List(Card(cardColors.BLUE, cardValues.THREE))))

    val topCard = Card(cardColors.RED, cardValues.TWO)
    controller.field = controller.field.copy(
      players = controller.field.players,
      topCard = topCard,
      currentPlayer = controller.field.currentPlayer
    )

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "play")
    }
  }

  test("processAction should throw an exception for an unknown action type") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val actionBuilder = mock(classOf[UnoActionBuilder])
    val processor = new ConcreteUnoActionProcessor(actionBuilder)
    val player = controller.field.players(controller.field.currentPlayer)

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "unknown")
    }
  }

  test("handleAction should invoke the correct handler from UnoActionBuilder") {
    val controller = mock(classOf[UnoController])
    val player = mock(classOf[IPlayer])
    val card = mock(classOf[Card])

    val processor = new ConcreteUnoActionProcessor(UnoActionBuilder.builder())
    val action = "play"

    val actionBuilder = UnoActionBuilder.builder().setAction(action).setCard(card)
    val handler = actionBuilder.build()
    handler.executeAction(controller, player)

    verify(controller, times(1)).play(card.asInstanceOf[Card])
  }

  test("completeAction should notify observers correctly for 'draw' and 'play' actions") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val actionBuilder = mock(classOf[UnoActionBuilder])
    val processor = new ConcreteUnoActionProcessor(actionBuilder)
    val player = controller.field.players(controller.field.currentPlayer)

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "play")
    }

    controller.notifyObservers(Event.Play)

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "draw")
    }

    controller.notifyObservers(Event.Draw)
  }

  test("completeAction should throw an exception for an unknown action type") {
    val controller = spy(new UnoController(createInitialField(), mock(classOf[IFileIo])))
    val actionBuilder = mock(classOf[UnoActionBuilder])
    val processor = new ConcreteUnoActionProcessor(actionBuilder)
    val player = controller.field.players(controller.field.currentPlayer)

    an[IllegalArgumentException] should be thrownBy {
      processor.completeAction(controller, player, "unknown")
    }
  }
}

/*
class ConcreteUnoActionProcessorSpec extends AnyFlatSpec with Matchers {

  "ConcreteUnoActionProcessor" should "validate and complete a 'play' action" in {
    val mockController = mock(classOf[UnoController])
    val mockPlayer = mock(classOf[IPlayer])
    val mockCard = Card(cardColors.RED, cardValues.FIVE)
    val mockField = mock(classOf[IUnoField])
    val mockActionBuilder = mock(classOf[UnoActionBuilder])
    val mockActionHandler = mock(classOf[UnoActionHandler])
    val mockAction = mock(classOf[UnoAction])

    when(mockActionBuilder.setAction("play")).thenReturn(mockActionBuilder)
    when(mockActionBuilder.setCard(mockCard)).thenReturn(mockActionBuilder)
    when(mockActionBuilder.build()).thenReturn(mockAction)
    when(mockPlayer.id).thenReturn(1)
    when(mockPlayer.valid(any())).thenReturn(true)
    when(mockController.field).thenReturn(mockField)
    when(mockField.topCard).thenReturn(mockCard)

    val processor = new ConcreteUnoActionProcessor(mockActionBuilder)

    println(s"Card to be played: ${mockField.topCard}")

    processor.processAction(mockController, mockPlayer, "play")

    verify(mockActionBuilder).setAction("play")
    verify(mockActionBuilder).setCard(mockCard)
    verify(mockAction).executeAction(mockController, mockPlayer)
    verify(mockController).notifyObservers(Event.Play)
  } // failed

  it should "throw an exception if 'play' action is invalid" in {
    val mockController = mock(classOf[UnoController])
    val mockPlayer = mock(classOf[IPlayer])
    val mockActionBuilder = mock(classOf[UnoActionBuilder])
    val mockCard = mock(classOf[ICard])
    val mockField = mock(classOf[IUnoField])

    when(mockPlayer.id).thenReturn(0)
    when(mockPlayer.valid(any())).thenReturn(false)
    when(mockController.field).thenReturn(mockField)
    when(mockField.topCard).thenReturn(mockCard)

    val processor = new ConcreteUnoActionProcessor(mockActionBuilder)

    val exception = intercept[IllegalArgumentException] {
      processor.processAction(mockController, mockPlayer, "play")
    }
    exception.getMessage should be ("Card does not fit.")
  } // passed

  it should "throw an exception for an unknown action" in {
    val mockController = mock(classOf[UnoController])
    val mockPlayer = mock(classOf[IPlayer])
    val mockActionBuilder = mock(classOf[UnoActionBuilder])
    val mockCard = mock(classOf[ICard])
    val mockField = mock(classOf[IUnoField])

    when(mockPlayer.id).thenReturn(1)
    when(mockController.field).thenReturn(mockField)
    when(mockField.topCard).thenReturn(mockCard)

    val processor = new ConcreteUnoActionProcessor(mockActionBuilder)

    val exception = intercept[IllegalArgumentException] {
      processor.processAction(mockController, mockPlayer, "unknown")
    }
    exception.getMessage should be ("Unknown action type")
  } // passed

  it should "validate and complete a 'draw' action" in {
    val mockController = mock(classOf[UnoController])
    val mockPlayer = mock(classOf[IPlayer])
    val mockActionBuilder = mock(classOf[UnoActionBuilder])
    val mockActionHandler = mock(classOf[UnoActionHandler])
    val mockAction = mock(classOf[UnoAction])
    val mockCard = mock(classOf[ICard])
    val mockField = mock(classOf[IUnoField])

    when(mockActionBuilder.setAction("draw")).thenReturn(mockActionBuilder)
    when(mockActionBuilder.build()).thenReturn(mockAction)
    when(mockPlayer.id).thenReturn(0)
    when(mockController.field).thenReturn(mockField)
    when(mockField.topCard).thenReturn(mockCard)

    val processor = new ConcreteUnoActionProcessor(mockActionBuilder)

    processor.processAction(mockController, mockPlayer, "draw")

    verify(mockActionBuilder).setAction("draw")
    verify(mockAction).executeAction(mockController, mockPlayer)
    verify(mockController).notifyObservers(Event.Draw)
  } // failed
}
*/
