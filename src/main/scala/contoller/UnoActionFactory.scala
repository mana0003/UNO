// UnoActionFactory.scala
package scala.controller
import scala.model.*

object UnoActionFactory {
  def getActionHandler(action: String, card: Option[Card] = None): UnoActionStrategy = action match {
    case "draw" => new DrawCardStrategy
    case "play" => card match {
      case Some(c) => new PlayCardStrategy(c)
      case None => throw new IllegalArgumentException("Card must be provided for play action")
    }
    case _ => throw new IllegalArgumentException("Unknown action type")
  }
}