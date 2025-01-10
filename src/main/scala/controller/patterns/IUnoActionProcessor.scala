package controller.patterns

import controller.controllerComponent.ControllerIm.UnoController
import controller.controllerComponent.IUnoController
import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.gameComponent.gameIm.Player

trait IUnoActionProcessor {
  def processAction(controller: UnoController, player: Player, action: String): Unit
  def validateAction(controller: UnoController, player: Player, action: String): Unit
  def completeAction(controller: UnoController, player: Player, action: String): Unit
  def handleAction(controller: UnoController, player: Player, action: String): Unit
}