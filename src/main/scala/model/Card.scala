package model

//import model.*
import view.*
//import scala.util.Event
//import scala.util.Observable
import controller.*
import scala.io._

enum cardColors {
  case RED, BLUE
  , YELLOW
  , GREEN
}

enum cardValues {
  case ZERO, ONE
  , TWO
  , THREE
  , FOUR
  , FIVE
  , SIX
  , SEVEN
  , EIGHT
  , NINE
  , SKIP
  , REVERSE
  , DRAW_TWO
  , WILD
  , WILD_DRAW_FOUR
}

case class Card(color: cardColors, value: cardValues) {
  // can card be played
  def canBePlayedOn(topCard: Card): Boolean = {
    this.color == topCard.color || this.value == topCard.value || this.value == cardValues.WILD || this.value == cardValues.WILD_DRAW_FOUR
  }

  def getColorCode: String = {
    this match {
      case Card(_, cardValues.WILD) => AnsiColor.WHITE
      case Card(_, cardValues.WILD_DRAW_FOUR) => AnsiColor.WHITE
      case Card(cardColors.RED, _) => AnsiColor.RED
      case Card(cardColors.GREEN, _) => AnsiColor.GREEN
      case Card(cardColors.YELLOW, _) => AnsiColor.YELLOW
      case Card(cardColors.BLUE, _) => AnsiColor.BLUE
    }
  }
}

def randomColor =
  cardColors.values.toList(scala.util.Random.nextInt(cardColors.values.length))

def randomValue =
  cardValues.values.toList(scala.util.Random.nextInt(cardValues.values.length))

def randomCard = Card(randomColor, randomValue)

def randomCards(i: Int): List[Card] = {
  (0 until i).map(_ => randomCard).toList
}