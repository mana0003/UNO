package controller.controllerComponent

import util.{Observer, Event}
import model.cardComponent.{cardColors, ICard}
import model.gameComponent.IUnoField
import util.{Event, Observer}
trait IUnoController {
  var field: IUnoField

  def addObserver(observer: Observer): Unit

  def notifyObservers(event: Event): Unit

  def setGuiActive(active: Boolean): Unit

  def isGuiMode: Boolean

  def play(card: ICard): Unit

  def draw(): Unit

  def undo(): Unit

  def redo(): Unit

  def startGame(): Unit

  def getCurrentPlayer: Int

  def getField: IUnoField

  def getChosenColor: Option[cardColors]

  def setChosenColor(color: Option[cardColors]): Unit
}