package controller.controllerComponent

<<<<<<< HEAD:src/main/scala/controller/IUnoController.scala
import util.{Observer, Event}
import model.{Card, UnoField}
import model.cardColors
=======
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.UnoField
import util.{Event, Observer}
>>>>>>> origin/main:src/main/scala/controller/controllerComponent/IUnoController.scala

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
  def getChosenColor: Option[cardColors]
}