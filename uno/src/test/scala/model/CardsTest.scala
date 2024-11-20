package scala.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import scala.model.*
import scala.view.*
import scala.util.*
import scala.io.AnsiColor

class CardTest extends AnyWordSpec {
  "A Card" should {
    "have a color" in {
      val card = Card(cardColors.RED, cardValues.ZERO)
      card.color should be(cardColors.RED)
    }
    "have a value" in {
      val card = Card(cardColors.RED, cardValues.ZERO)
      card.value should be(cardValues.ZERO)
    }
    "have a color code" in {
      val redCard = Card(cardColors.RED, cardValues.ZERO)
      redCard.getColorCode should be(AnsiColor.RED)
      val greenCard = Card(cardColors.GREEN, cardValues.ZERO)
      greenCard.getColorCode should be(AnsiColor.GREEN)
      val blueCard = Card(cardColors.BLUE, cardValues.ZERO)
      blueCard.getColorCode should be(AnsiColor.BLUE)
      val yellowCard = Card(cardColors.YELLOW, cardValues.ZERO)
      yellowCard.getColorCode should be(AnsiColor.YELLOW)
      val wildCard = Card(cardColors.RED, cardValues.WILD)
      wildCard.getColorCode should be(AnsiColor.WHITE)
      val wildDrawFourCard = Card(cardColors.RED, cardValues.WILD_DRAW_FOUR)
      wildDrawFourCard.getColorCode should be(AnsiColor.WHITE)
    }
    "be able to be played on a card of the same color" in {
      val card1 = Card(cardColors.RED, cardValues.ZERO)
      val card2 = Card(cardColors.RED, cardValues.ONE)
      card1.canBePlayedOn(card2) should be(true)
    }
    "be able to be played on a card of the same value" in {
      val card1 = Card(cardColors.RED, cardValues.ZERO)
      val card2 = Card(cardColors.BLUE, cardValues.ZERO)
      card1.canBePlayedOn(card2) should be(true)
    }
    "not be able to be played on a card of a different color and value" in {
      val card1 = Card(cardColors.RED, cardValues.ZERO)
      val card2 = Card(cardColors.BLUE, cardValues.ONE)
      card1.canBePlayedOn(card2) should be(false)
    }
    "be able to be played on any card if it is a wild card" in {
      val card1 = Card(cardColors.RED, cardValues.WILD)
      val card2 = Card(cardColors.BLUE, cardValues.ONE)
      card1.canBePlayedOn(card2) should be(true)
    }
    "give random color" in {
      val color = randomColor
      List(
        cardColors.RED,
        cardColors.BLUE,
        cardColors.GREEN,
        cardColors.YELLOW
      ) should contain(color)
    }
    "give random value" in {
      val value = randomValue
      List(
        cardValues.ZERO,
        cardValues.ONE,
        cardValues.TWO,
        cardValues.THREE,
        cardValues.FOUR,
        cardValues.FIVE,
        cardValues.SIX,
        cardValues.SEVEN,
        cardValues.EIGHT,
        cardValues.NINE,
        cardValues.SKIP,
        cardValues.REVERSE,
        cardValues.DRAW_TWO,
        cardValues.WILD,
        cardValues.WILD_DRAW_FOUR
      ) should contain(value)
    }
    "give random card" in {
      val card = randomCard
      List(
        cardColors.RED,
        cardColors.BLUE,
        cardColors.GREEN,
        cardColors.YELLOW
      ) should contain(card.color)
      List(
        cardValues.ZERO,
        cardValues.ONE,
        cardValues.TWO,
        cardValues.THREE,
        cardValues.FOUR,
        cardValues.FIVE,
        cardValues.SIX,
        cardValues.SEVEN,
        cardValues.EIGHT,
        cardValues.NINE,
        cardValues.SKIP,
        cardValues.REVERSE,
        cardValues.DRAW_TWO,
        cardValues.WILD,
        cardValues.WILD_DRAW_FOUR
      ) should contain(card.value)
    }
    "give list of random cards" in {
      val cards = randomCards(5)
      cards should have length 5
      cards.foreach { card =>
        List(
          cardColors.RED,
          cardColors.BLUE,
          cardColors.GREEN,
          cardColors.YELLOW
        ) should contain(card.color)
        List(
          cardValues.ZERO,
          cardValues.ONE,
          cardValues.TWO,
          cardValues.THREE,
          cardValues.FOUR,
          cardValues.FIVE,
          cardValues.SIX,
          cardValues.SEVEN,
          cardValues.EIGHT,
          cardValues.NINE,
          cardValues.SKIP,
          cardValues.REVERSE,
          cardValues.DRAW_TWO,
          cardValues.WILD,
          cardValues.WILD_DRAW_FOUR
        ) should contain(card.value)
      }
    }
  }
}