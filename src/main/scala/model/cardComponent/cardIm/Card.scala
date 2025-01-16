package model.cardComponent.cardIm

import com.google.inject.{AbstractModule, Guice, Inject}
import net.codingwell.scalaguice.ScalaModule
import model.cardComponent.{ICard, cardColors, cardValues}
import model.cardComponent.cardValues.WILD
import scalafx.scene.paint.Color


class Card @Inject() (color: cardColors, value: cardValues) extends ICard {
  def getColor: cardColors = color
  def canBePlayed(topCard: ICard): Boolean = {
    this.getColor == topCard.getColor || this.getValue == topCard.getValue || this.getValue == cardValues.WILD || this.getValue == cardValues.WILD_DRAW_FOUR
  }

  def getColorCode: Color = {
    color match {
      case cardColors.RED => Color.Red
      case cardColors.GREEN => Color.Green
      case cardColors.YELLOW => Color.Yellow
      case cardColors.BLUE => Color.Blue
      case _ => Color.Black
    }
  }

  def getValue: cardValues = value

  def copy(color: cardColors): ICard = {
    Card(color, getValue)
  }
}

//def randomColor: cardColors = cardColors.values.toList(scala.util.Random.nextInt(cardColors.values.length))
def randomColor(cardValue: cardValues): cardColors = {
  if (cardValue == cardValues.WILD || cardValue == cardValues.WILD_DRAW_FOUR) {
    cardColors.BLACK
  } else {
    cardColors.values.toList(scala.util.Random.nextInt(cardColors.values.length))
  }
}
def randomValue: cardValues = {
  cardValues.values.toList(scala.util.Random.nextInt(cardValues.values.length))
}

//def randomValue: cardValues = cardValues.values.toList(scala.util.Random.nextInt(cardValues.values.length))

/*def randomCard: Card = {
  Card(randomColor, randomValue)
}*/

def randomCard: Card = {
  val value = randomValue
  val color = randomColor(value) // Use the value to determine the color
  Card(color, value)
}

def randomCards(i: Int): List[Card] = {
  (0 until i).map(_ => randomCard).toList
}