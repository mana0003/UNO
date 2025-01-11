package model.cardComponent.cardIm

import com.google.inject.{AbstractModule, Guice, Inject}
import net.codingwell.scalaguice.ScalaModule
import model.cardComponent.{ICard, cardColors, cardValues}
import scalafx.scene.paint.Color


class Card @Inject() (color: cardColors, value: cardValues) extends ICard {
  def getColor: cardColors = color
  def canBePlayed(topCard: ICard): Boolean = {
    this.color == topCard.getColor || this.value == topCard.getValue || this.value == cardValues.WILD || this.value == cardValues.WILD_DRAW_FOUR
  }

  def getColorCode: Color = {
    color match {
      case cardColors.RED => Color.Red
      case cardColors.GREEN => Color.Green
      case cardColors.YELLOW => Color.Yellow
      case cardColors.BLUE => Color.Blue
      case null => Color.Black
    }
  }

  def getValue: cardValues = value

  def copy(color: cardColors): ICard = {
    Card(color, getValue)
  }
}

def randomColor: cardColors = cardColors.values.toList(scala.util.Random.nextInt(cardColors.values.length))

def randomValue: cardValues = cardValues.values.toList(scala.util.Random.nextInt(cardValues.values.length))

def randomCard: Card = {
  Card(randomColor, randomValue)
}

def randomCards(i: Int): List[Card] = {
  (0 until i).map(_ => randomCard).toList
}