package controller.patterns

import controller.controllerComponent.IUnoController
//import controller.patterns.IUnoActionBuilder
import model.*
import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.cardComponent.cardIm.Card

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
        case "play" =>
          if (card == null) throw new IllegalStateException("Card must be set for play action")
          new PlayAction(card)
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
      //controller.play(card.asInstanceOf[Card])
      controller.play(card.asInstanceOf[Card])
    }
  }

  class DrawAction extends UnoAction {
    override def executeAction(controller: IUnoController, player: IPlayer): Unit = {
      controller.draw()
    }
  }
}