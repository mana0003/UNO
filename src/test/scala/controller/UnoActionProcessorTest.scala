package controller

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

class UnoActionProcessorTest extends AnyFunSuite with Matchers {

  test("processAction should successfully process a 'play' action for a valid card") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val topCard = mock[Card]
    val validCard = mock[Card]

    when(controller.field.topCard).thenReturn(topCard)
    when(player.valid(topCard)).thenReturn(true)

    val processor = new ConcreteUnoActionProcessor

    noException shouldBe thrownBy {
      processor.processAction(controller, player, "play")
    }

    verify(player).valid(topCard)
    verify(controller).notifyObservers(Event.Play)
  }

  test("processAction should throw an exception for an invalid 'play' action") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val topCard = mock[Card]

    when(controller.field.topCard).thenReturn(topCard)
    when(player.valid(topCard)).thenReturn(false)

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "play")
    }

    verify(player).valid(topCard)
    verify(controller, never()).notifyObservers(any())
  }

  test("processAction should throw an exception for an unknown action type") {
    val controller = mock[UnoController]
    val player = mock[Player]

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.processAction(controller, player, "unknown")
    }

    verify(controller, never()).notifyObservers(any())
  }

  test("handleAction should invoke the correct handler from UnoActionBuilder") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val action = "play"

    val handler = mock[UnoActionBuilder.UnoAction]
    val builder = mock[UnoActionBuilder.UnoActionBuilder]

    // Mock the builder
    when(UnoActionBuilder.builder()).thenReturn(builder)
    when(builder.setAction(action)).thenReturn(builder)
    when(builder.build()).thenReturn(handler)

    val processor = new ConcreteUnoActionProcessor

    processor.handleAction(controller, player, action)

    verify(handler).executeAction(controller, player)
  }

  test("completeAction should notify observers correctly for 'draw' and 'play' actions") {
    val controller = mock[UnoController]
    val player = mock[Player]

    val processor = new ConcreteUnoActionProcessor

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "play")
    }

    verify(controller).notifyObservers(Event.Play)

    noException shouldBe thrownBy {
      processor.completeAction(controller, player, "draw")
    }

    verify(controller).notifyObservers(Event.Draw)
  }

  test("completeAction should throw an exception for an unknown action type") {
    val controller = mock[UnoController]
    val player = mock[Player]

    val processor = new ConcreteUnoActionProcessor

    an[IllegalArgumentException] should be thrownBy {
      processor.completeAction(controller, player, "unknown")
    }

    verify(controller, never()).notifyObservers(any())
  }
}
