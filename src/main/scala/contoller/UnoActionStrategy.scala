// UnoActionStrategy.scala
package scala.controller

import scala.model.*

trait UnoActionStrategy {
  def executeAction(controller: UnoController, player: Player): Unit
}

class DrawCardStrategy extends UnoActionStrategy {
  override def executeAction(controller: UnoController, player: Player): Unit = {
    println(s"${player.id} draws a card")
    controller.draw()
  }
}

class PlayCardStrategy(card: Card) extends UnoActionStrategy {
  override def executeAction(controller: UnoController, player: Player): Unit = {
    println(s"${player.id} plays a card")
    controller.play(card)
  }
}

class UnoActionHandler(var strategy: UnoActionStrategy) {
  def setStrategy(strategy: UnoActionStrategy): Unit = {
    this.strategy = strategy
  }

  def executeStrategy(controller: UnoController, player: Player): Unit = {
    strategy.executeAction(controller, player)
  }
}