package model

trait ICard {
  def canBePlayedOn(topCard: Card): Boolean
  def getColorCode: String
}