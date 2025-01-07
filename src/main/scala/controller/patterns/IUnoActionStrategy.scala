package controller.patterns

import controller.controllerComponent.IUnoController
import controller.patterns.patternsIm.UnoActionStrategy
import model.gameComponent.gameIm.Player


trait IUnoActionStrategy {
  def executeAction(controller: IUnoController, player: Player): Unit
  def setStrategy(strategy: UnoActionStrategy): Unit
  def executeStrategy(controller: IUnoController, player: Player): Unit
}