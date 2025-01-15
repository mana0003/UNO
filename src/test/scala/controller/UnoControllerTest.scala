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
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.gameComponent.gameIm
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
    val controller = UnoController(field)

    "notify its observers on start" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = TestObserver(controller)
      testObserver.bing should be(false)
      controller.notifyObservers(Event.Start)
      testObserver.bing should be(true)
    }

    "notify its observers on quit" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.addObserver(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = TestObserver(controller)
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
      val testObserver = TestObserver(controller)
      testObserver.controller.removeObserver(testObserver)
      testObserver.bing should be(false)
      controller.notifyObservers(Event.Start)
      testObserver.bing should be(false)
    }
    "play a card" in {
      val card = Card(RED, ONE)
      controller.play(card)
      controller.field.players.head.hand.cards should not contain card
    }
    "draw a card" in {
      controller.draw()
      controller.field.players.head.hand.cards.size should be(2)
      controller.field = UnoField(
        List(gameIm.Player(0, PlayerHand(List(Card(RED, ONE), Card(BLUE, TWO))))), // players
        topCard = Card(RED, THREE), // You need to specify a valid topCard here
        currentPlayer = 0 // Specify the current player
      )
    }
    "quit the game" in {
      class TestObserver(val controller: UnoController) extends Observer {
      controller.addObserver(this)
      var bing: Event = Start
      def update(e: Event): Unit = bing = e
      }
      val testObserver = TestObserver(controller)
      val cards = controller.field.players.head.hand.cards
      controller.play(cards.head)
      controller.play(cards(1))
      testObserver.bing should be(Event.Quit)
    }
  }
}