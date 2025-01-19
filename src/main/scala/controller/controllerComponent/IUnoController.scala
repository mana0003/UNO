package controller.controllerComponent

import model.cardComponent.ICard
import model.gameComponent.gameIm.UnoField
import model.gameComponent.IUnoField
import util.{Event, Observer}
//import controller.controllerComponent.*

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
  def getField: IUnoField
  def getCurrentPlayer: Int
  //def getChosenColor: Option[cardColors]
  def getChosenColor: Option[model.cardComponent.cardColors]
  def setChosenColor(color: Option[model.cardComponent.cardColors]): Unit
  def saveGame(): Unit
  def loadGame(): Unit
}