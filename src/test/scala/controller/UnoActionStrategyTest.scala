// UnoActionStrategySpec.scala
package controller
import controller.patterns.{DrawCardStrategy, PlayCardStrategy, UnoActionHandler}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.*
import controller.controllerComponent.IUnoController
import model.cardComponent.cardColors.RED
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.IPlayer
import model.cardComponent.cardIm.Card
import model.cardComponent.cardValues.ONE

class UnoActionStrategyTest extends AnyFlatSpec with Matchers with MockitoSugar {

  "A DrawCardStrategy" should "execute draw action" in {
    val controller = mock[IUnoController]
    val player = mock[IPlayer]
    when(player.id).thenReturn(1)

    val strategy = new DrawCardStrategy
    strategy.executeAction(controller, player)

    verify(controller).draw()
  }

  "A PlayCardStrategy" should "execute play action" in {
  val controller = mock[IUnoController]
  val player = mock[IPlayer]
  val card = new Card(color = RED, value = ONE) // Create a real instance of Card
  when(player.id).thenReturn(1)

  val strategy = new PlayCardStrategy(card)
  strategy.executeAction(controller, player)

  verify(controller).play(card)
}

  "An UnoActionHandler" should "set and execute strategy" in {
    val controller = mock[IUnoController]
    val player = mock[IPlayer]
    val drawStrategy = mock[DrawCardStrategy]
    val playStrategy = mock[PlayCardStrategy]
    when(player.id).thenReturn(1)

    val handler = new UnoActionHandler(drawStrategy)
    handler.executeStrategy(controller, player)
    verify(drawStrategy).executeAction(controller, player)

    handler.setStrategy(playStrategy)
    handler.executeStrategy(controller, player)
    verify(playStrategy).executeAction(controller, player)
  }
} // done