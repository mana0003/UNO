package controller

import model.*
import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._


class UnoActionStrategyTest extends AnyFunSuite with Matchers {

  test("DrawCardStrategy should invoke draw on the controller") {
    val controller = mock[UnoController]
    val player = mock[Player]

    val strategy = new DrawCardStrategy
    strategy.executeAction(controller, player)

    verify(controller).draw()
    // Additional output verification if required
  }

  test("PlayCardStrategy should invoke play on the controller with the specified card") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val card = mock[Card]

    val strategy = new PlayCardStrategy(card)
    strategy.executeAction(controller, player)

    verify(controller).play(card)
    // Additional output verification if required
  }

  test("UnoActionHandler should execute the assigned DrawCardStrategy") {
    val controller = mock[UnoController]
    val player = mock[Player]

    val drawStrategy = new DrawCardStrategy
    val actionHandler = new UnoActionHandler(drawStrategy)

    actionHandler.executeStrategy(controller, player)

    verify(controller).draw()
  }

  test("UnoActionHandler should execute the assigned PlayCardStrategy") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val card = mock[Card]

    val playStrategy = new PlayCardStrategy(card)
    val actionHandler = new UnoActionHandler(playStrategy)

    actionHandler.executeStrategy(controller, player)

    verify(controller).play(card)
  }

  test("UnoActionHandler should allow strategy swapping at runtime") {
    val controller = mock[UnoController]
    val player = mock[Player]
    val card = mock[Card]

    val drawStrategy = new DrawCardStrategy
    val playStrategy = new PlayCardStrategy(card)

    val actionHandler = new UnoActionHandler(drawStrategy)

    // Initially set to DrawCardStrategy
    actionHandler.executeStrategy(controller, player)
    verify(controller).draw()

    // Swap to PlayCardStrategy
    actionHandler.setStrategy(playStrategy)
    actionHandler.executeStrategy(controller, player)
    verify(controller).play(card)
  }
}
