package controller.patterns.patternsIm

import controller.controllerComponent.IUnoController
//import controller.patterns.IUnoActionBuilder
import model.*
import model.cardComponent.ICard
import model.gameComponent.IPlayer

object UnoActionBuilder {
  def builder(): UnoActionBuilder = new UnoActionBuilder

  class UnoActionBuilder {
    private var action: String = _
    private var card: ICard = _

    def setAction(action: String): UnoActionBuilder = {
      this.action = action
      this
    }

    def setCard(card: ICard): UnoActionBuilder = {
      this.card = card
      this
    }

    def build(): UnoAction = {
      action match {
        case "play" => new PlayAction(card)
        case "draw" => new DrawAction
        case _ => throw new IllegalArgumentException("Unknown action")
      }
    }
  }

  trait UnoAction {
    def executeAction(controller: IUnoController, player: IPlayer): Unit
  }

  class PlayAction(card: ICard) extends UnoAction {
    override def executeAction(controller: IUnoController, player: IPlayer): Unit = {
      controller.play(card)
    }
  }

  class DrawAction extends UnoAction {
    override def executeAction(controller: IUnoController, player: IPlayer): Unit = {
      controller.draw()
    }
  }
}