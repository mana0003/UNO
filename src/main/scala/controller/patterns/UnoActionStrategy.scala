// UnoActionStrategy.scala
package controller.patterns

import controller.controllerComponent.IUnoController
//import model.*
import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.cardComponent.cardIm.Card

trait UnoActionStrategy {
  def executeAction(controller: IUnoController, player: IPlayer): Unit
}

class DrawCardStrategy extends UnoActionStrategy {
  override def executeAction(controller: IUnoController, player: IPlayer): Unit = {
    println(s"${player.id} draws a card")
    controller.draw()
  }
}

class PlayCardStrategy(card: ICard) extends UnoActionStrategy {
  override def executeAction(controller: IUnoController, player: IPlayer): Unit = {
    println(s"${player.id} plays a card")
    controller.play(card.asInstanceOf[Card])
  }
}

class UnoActionHandler(var strategy: UnoActionStrategy) {
  def setStrategy(strategy: UnoActionStrategy): Unit = {
    this.strategy = strategy
  }

  def executeStrategy(controller: IUnoController, player: IPlayer): Unit = {
    strategy.executeAction(controller, player)
  }
}
