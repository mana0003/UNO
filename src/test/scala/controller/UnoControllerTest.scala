package controller

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import view.*
import model.*
import util.*
import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import model.cardComponent.cardValues.*
import model.cardComponent.cardColors.*
import model.cardComponent.cardIm.Card
import model.fileIoComponent.IFileIo
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.gameComponent.gameIm
import org.mockito.Mockito.{mock, spy}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import util.Event.*

class UnoControllerTest extends AnyWordSpec {

  "The Controller" should {
    val field =
      UnoField(
        List(gameIm.Player(0, PlayerHand(List(Card(RED, ONE), Card(RED, TWO))))),
        Card(RED, THREE),
        currentPlayer = 0
      )
    //val controller = UnoController(field)
    val controller = spy(UnoController(field, mock(classOf[IFileIo])))


    "notify its observers on start" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.bing should be(false)
      controller.notifyObservers(Event.Start)
      testObserver.bing should be(true)
    }

    "set the gui active" in {
      controller.setGuiActive(true)
      controller.isGuiMode should be(true)
    }
    "notify its observers on quit" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.bing should be(false)
      controller.notifyObservers(Event.Quit)
      testObserver.bing should be(true)
    }

    "don't notify its observers on remove" in {
      class TestObserver(val controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.controller.removeObserver(testObserver)
      testObserver.bing should be(false)
      controller.notifyObservers(Event.Start)
      testObserver.bing should be(false)
    }
    "play a card" in {
      val card = Card(RED, ONE)
      controller.play(card)
      controller.field.players(controller.field.currentPlayer).hand.cards should not contain card
      controller.field.players.head.hand.cards should not contain card
    }
    "draw a card" in {
      /*controller.draw()
      controller.field.players.head.hand.cards.size should be(2)
      controller.field = UnoField(
        List(gameIm.Player(0, PlayerHand(List(Card(RED, ONE), Card(BLUE, TWO))))), // players
        topCard = Card(RED, THREE),
        currentPlayer = 0 // Specify the current player
      )*/
      val initialHandSize = controller.field.players.head.hand.cards.size
      controller.draw()
      controller.field.players.head.hand.cards.size should be(initialHandSize + 1)

    }
    "quit the game" in {
      class TestObserver(val controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing: Event = Start

        def update(e: Event): Unit = bing = e
      }
      val testObserver = new TestObserver(controller)
      controller.notifyObservers(Event.Quit)
      //val cards = controller.field.players.head.hand.cards
      //controller.play(cards.head)
      //controller.play(cards(1))
      testObserver.bing should be(Event.Quit)
    }

    "undo the last action" in {
      val initialHandSize = controller.field.players.head.hand.cards.size
      controller.draw()
      controller.field.players.head.hand.cards.size should be(initialHandSize + 1)
      controller.undo()
      controller.field.players.head.hand.cards.size should be(initialHandSize)
    }

    "redo the last action" in {
      val initialHandSize = controller.field.players.head.hand.cards.size
      controller.draw()
      controller.field.players.head.hand.cards.size should be(initialHandSize + 1)
      controller.undo()
      controller.field.players.head.hand.cards.size should be(initialHandSize)
      controller.redo()
      controller.field.players.head.hand.cards.size should be(initialHandSize + 1)
    }
    // a test for this
    // override def startGame(): Unit = {
    //    notifyObservers(Event.Start)
    //  }
    "start the game" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.bing should be(false)
      controller.startGame()
      testObserver.bing should be(true)
    }

    "notify Observer when save" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.bing should be(false)
      controller.saveGame()
      testObserver.bing should be(true)
    }
    "notify Observer when load" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = new TestObserver(controller)
      testObserver.bing should be(false)
      controller.loadGame()
      testObserver.bing should be(true)
    }
    "getChosenColor" in {
      controller.setChosenColor(Some(RED))
      controller.getChosenColor should be(Some(RED))
    }
    "setChosenColor" in {
      controller.setChosenColor(Some(RED))
      controller.getChosenColor should be(Some(RED))
    }
    "getField" in {
      controller.field = field
      controller.getField should be(field)
    }
    "getCurrentPlayer" in {
      controller.getCurrentPlayer should be(0)
    }
  }
}