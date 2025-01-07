// UnoActionStrategy.scala
package controller.patterns.patternsIm

import controller.controllerComponent.IUnoController
import controller.patterns.UnoActionStrategy
import model.*
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.Player

trait UnoActionStrategy {
  def executeAction(controller: IUnoController, player: Player): Unit
}

class DrawCardStrategy extends UnoActionStrategy {
  override def executeAction(controller: IUnoController, player: Player): Unit = {
    println(s"${player.id} draws a card")
    controller.draw()
  }
}

class PlayCardStrategy(card: Card) extends UnoActionStrategy {
  override def executeAction(controller: IUnoController, player: Player): Unit = {
    println(s"${player.id} plays a card")
    controller.play(card)
  }
}

class UnoActionHandler(var strategy: UnoActionStrategy) {
  def setStrategy(strategy: UnoActionStrategy): Unit = {
    this.strategy = strategy
  }

  def executeStrategy(controller: IUnoController, player: Player): Unit = {
    strategy.executeAction(controller, player)
  }
}