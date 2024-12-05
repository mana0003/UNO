// UnoActionProcessor.scala
package scala.controller
import scala.model.*

class UnoActionBuilder {
  private var action: String = _
  private var card: Option[Card] = None

  def setAction(action: String): UnoActionBuilder = {
    this.action = action
    this
  }

  def setCard(card: Card): UnoActionBuilder = {
    this.card = Some(card)
    this
  }

  def build(): UnoActionStrategy = {
    action match {
      case "draw" => new DrawCardStrategy
      case "play" => card match {
        case Some(c) => new PlayCardStrategy(c)
        case None => throw new IllegalArgumentException("Card must be provided for play action")
      }
      case _ => throw new IllegalArgumentException("Unknown action type")
    }
  }
}

object UnoActionFactory {
  def builder(): UnoActionBuilder = new UnoActionBuilder
}