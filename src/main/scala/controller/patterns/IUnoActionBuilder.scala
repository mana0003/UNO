package controller.patterns

import controller.controllerComponent.IUnoController
import controller.patterns.patternsIm.UnoActionBuilder.UnoActionBuilder
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.Player

trait IUnoActionBuilder {
  def builder(): IUnoActionBuilder
  def setAction(action: String): UnoActionBuilder
  def setCard(card: Card): UnoActionBuilder
  def executeAction(controller: IUnoController, player: Player): Unit
}