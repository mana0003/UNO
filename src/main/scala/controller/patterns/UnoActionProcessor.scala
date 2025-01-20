
package controller.patterns

import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import model.*
import model.gameComponent.IPlayer
import util.Event

abstract class UnoActionProcessor() {
  def processAction(controller: UnoController, player: IPlayer, action: String): Unit = {
    validateAction(controller, player, action)
    handleAction(controller, player, action)
    completeAction(controller, player, action)
  }
  //def handleAction(controller: UnoController, player: IPlayer, action: String): Unit

  private def validateAction(controller: UnoController, player: IPlayer, action: String): Unit = {
    println(s"Validating action: $action for player: ${player.id}")
    action match {
      case "play" =>
        // check if player played a valid card
        if (!player.valid(controller.field.topCard)) {
          throw new IllegalArgumentException("Card does not fit.")
        }
      case _ =>
        throw new IllegalArgumentException("Unknown action type")
    }
  }

  def completeAction(controller: UnoController, player: IPlayer, action: String): Unit = {
    println(s"Completing action: $action for player: ${player.id}")
    action match {
      case "draw" =>
        // Notify observers that a card has been drawn
        controller.notifyObservers(Event.Draw)
      case "play" =>
        // Notify observers that a card has been played
        controller.notifyObservers(Event.Play)
      case _ =>
        throw new IllegalArgumentException("Unknown action type")
    }
  }

  def handleAction(controller: UnoController, player: IPlayer, action: String): Unit
}

class ConcreteUnoActionProcessor extends UnoActionProcessor {
  override def handleAction(controller: UnoController, player: IPlayer, action: String): Unit = {
    val handler = UnoActionBuilder.builder().setAction(action).build()
    handler.executeAction(controller, player)
  }
}
