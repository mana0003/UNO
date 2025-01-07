package controller.patterns.patternsIm

import controller.controllerComponent.IUnoController
import model.*
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.Player

object UnoActionBuilder {
  def builder(): UnoActionBuilder = new UnoActionBuilder

  class UnoActionBuilder {
    private var action: String = _
    private var card: Card = _

    def setAction(action: String): UnoActionBuilder = {
      this.action = action
      this
    }

    def setCard(card: Card): UnoActionBuilder = {
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
    def executeAction(controller: IUnoController, player: Player): Unit
  }

  class PlayAction(card: Card) extends UnoAction {
    override def executeAction(controller: IUnoController, player: Player): Unit = {
      controller.play(card)
    }
  }

  class DrawAction extends UnoAction {
    override def executeAction(controller: IUnoController, player: Player): Unit = {
      controller.draw()
    }
  }
}