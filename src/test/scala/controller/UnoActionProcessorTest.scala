package controller

import controller.*
import controller.controllerComponent.IUnoController
import controller.controllerComponent.ControllerIm.UnoController
import controller.patterns.UnoActionBuilder.UnoActionBuilder
import controller.patterns.UnoActionBuilder.UnoAction
import controller.patterns.*
import model.*
import model.cardComponent.cardIm.Card
import model.cardComponent.cardValues.SEVEN
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.{IPlayer, IPlayerHand}
import model.cardComponent.{ICard, cardColors, cardValues}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import util.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.*
import model.fileIoComponent.IFileIo

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
