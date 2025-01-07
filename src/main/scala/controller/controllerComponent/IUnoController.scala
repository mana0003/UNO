package controller.controllerComponent

import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.UnoField
import util.{Event, Observer}

trait IUnoController {
  def addObserver(observer: Observer): Unit
  def notifyObservers(event: Event): Unit
  def setGuiActive(active: Boolean): Unit
  def isGuiMode: Boolean
  def play(card: Card): Unit
  def draw(): Unit
  def undo(): Unit
  def redo(): Unit
  def startGame(): Unit
  def getField: UnoField
  def getCurrentPlayer: Int
}