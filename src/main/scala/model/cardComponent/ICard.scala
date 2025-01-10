package model.cardComponent

import scalafx.scene.paint.Color
import model.cardComponent.cardColors.cardColors
import model.cardComponent.cardValues.cardValues

trait ICard {
  def canBePlayedOn(topCard: ICard): Boolean
  def getColorCode: Color
  def getColor: cardColors.cardColors
  def getValue: cardValues.cardValues
  def getCard: ICard
}