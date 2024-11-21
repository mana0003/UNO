package scala.controller

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import scala.view.*
import scala.model.*
import scala.util.*
import scala.controller.*
import scala.model.cardValues.*
import scala.model.cardColors.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import scala.util.Event.*

class ControllerTest extends AnyWordSpec {
  "The Controller" should {
    val field =
      UnoField(
        List(Player(0, PlayerHand(List(Card(RED, ONE), Card(RED, TWO))))),
        Card(RED, THREE)
      )
    val controller = UnoController(field)

    "notify its observers on start" in {
      class TestObserver(controller: UnoController) extends Observer {
        controller.add(this)
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
        controller.add(this)
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
        controller.add(this)
        var bing = false

        def update(e: Event): Unit = bing = true
      }
      val testObserver = TestObserver(controller)
      testObserver.controller.remove(testObserver)
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
      controller.field =
        UnoField(List(Player(0, PlayerHand(List(Card(RED, ONE), Card(BLUE, TWO))))))
    }
    "quit the game" in {
      class TestObserver(val controller: UnoController) extends Observer {
      controller.add(this)
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