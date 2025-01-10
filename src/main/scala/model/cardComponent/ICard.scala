package model.cardComponent

import scalafx.scene.paint.Color
import model.cardComponent.cardIm.{cardColors, cardValues}

trait ICard {
  def getColor: Option[cardColors]
  def value: cardValues
  def canBePlayed(topCard: ICard): Boolean
  def getColorCode: Color
  def getValue: cardValues
  def copy(color: Option[cardColors]): ICard
}
enum cardColors {
  case RED, BLUE, YELLOW, GREEN
}
enum cardValues {
  case ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
}