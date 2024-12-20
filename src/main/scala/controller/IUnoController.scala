package controller

import model.{Card, UnoField}
import util.{Event, Observer}

trait IUnoController {
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