package model.cardComponent.cardIm

import com.google.inject.{AbstractModule, Guice, Inject}
import net.codingwell.scalaguice.ScalaModule
import model.cardComponent.ICard
import scalafx.scene.paint.Color

enum cardColors {
  case RED, BLUE, YELLOW, GREEN
}

enum cardValues {
  case ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR
}

case class Card @Inject() (color: Option[cardColors], value: cardValues) extends ICard {
  def getColor: Option[cardColors] = color

  def canBePlayed(topCard: ICard): Boolean = {
    this.color == topCard.getColor || this.value == topCard.getValue || this.value == cardValues.WILD || this.value == cardValues.WILD_DRAW_FOUR
  }

  def getColorCode: Color = {
    color match {
      case Some(cardColors.RED) => Color.Red
      case Some(cardColors.GREEN) => Color.Green
      case Some(cardColors.YELLOW) => Color.Yellow
      case Some(cardColors.BLUE) => Color.Blue
      case _ => Color.Black
    }
  }

  def getValue: cardValues = value

  def copy(color: Option[cardColors]): ICard = {
    Card(color, getValue)
  }
}

def randomColor: cardColors = cardColors.values.toList(scala.util.Random.nextInt(cardColors.values.length))

def randomValue: cardValues = cardValues.values.toList(scala.util.Random.nextInt(cardValues.values.length))

def randomCard: Card = {
  new Card(Some(randomColor), randomValue)
}

def randomCards(i: Int): List[Card] = {
  (0 until i).map(_ => randomCard).toList
}