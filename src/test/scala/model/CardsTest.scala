package model

import org.scalatest.funsuite.AnyFunSuite
//import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import model.*
//import view.*
//import util.*
//import scala.io.AnsiColor

import scalafx.scene.paint.Color

class CardsTest extends AnyFunSuite {

  // Test case for the canBePlayedOn method
  test("canBePlayedOn should return true if the color or value matches or if it's a Wild card") {
    val redCard = Card(cardColors.RED, cardValues.THREE)
    val blueCard = Card(cardColors.BLUE, cardValues.THREE)
    val greenCard = Card(cardColors.GREEN, cardValues.FIVE)
    val wildCard = Card(cardColors.RED, cardValues.WILD)

    assert(redCard.canBePlayedOn(blueCard)) // Same value
    assert(blueCard.canBePlayedOn(redCard)) // Same value
    assert(greenCard.canBePlayedOn(redCard)) // Different color and value
    assert(wildCard.canBePlayedOn(redCard)) // Wild card can always be played
    assert(redCard.canBePlayedOn(wildCard)) // Wild card can always be played
  }

  // Test case for the getColorCode method
  test("getColorCode should return correct color based on the card color or wild type") {
    val redCard = Card(cardColors.RED, cardValues.THREE)
    val greenCard = Card(cardColors.GREEN, cardValues.SKIP)
    val yellowCard = Card(cardColors.YELLOW, cardValues.REVERSE)
    val blueCard = Card(cardColors.BLUE, cardValues.ZERO)
    val wildCard = Card(cardColors.RED, cardValues.WILD)
    val wildDrawFourCard = Card(cardColors.GREEN, cardValues.WILD_DRAW_FOUR)

    assert(redCard.getColorCode == Color.Red)
    assert(greenCard.getColorCode == Color.Green)
    assert(yellowCard.getColorCode == Color.Yellow)
    assert(blueCard.getColorCode == Color.Blue)
    assert(wildCard.getColorCode == Color.Black)
    assert(wildDrawFourCard.getColorCode == Color.Black)
  }

  // Test case for randomCard generation
  test("randomCard should return a Card with valid color and value") {
    val card = randomCard
    assert(card.color == cardColors.RED || card.color == cardColors.GREEN ||
      card.color == cardColors.YELLOW || card.color == cardColors.BLUE)
    assert(card.value == cardValues.ZERO || card.value == cardValues.ONE ||
      card.value == cardValues.TWO || card.value == cardValues.THREE ||
      card.value == cardValues.FOUR || card.value == cardValues.FIVE ||
      card.value == cardValues.SIX || card.value == cardValues.SEVEN ||
      card.value == cardValues.EIGHT || card.value == cardValues.NINE ||
      card.value == cardValues.SKIP || card.value == cardValues.REVERSE ||
      card.value == cardValues.DRAW_TWO || card.value == cardValues.WILD ||
      card.value == cardValues.WILD_DRAW_FOUR)
  }

  // Test case for randomCards generation
  test("randomCards should return a list of random Cards with valid colors and values") {
    val cards = randomCards(5)
    assert(cards.length == 5)
    cards.foreach(card => {
      assert(card.color == cardColors.RED || card.color == cardColors.GREEN ||
        card.color == cardColors.YELLOW || card.color == cardColors.BLUE)
      assert(card.value == cardValues.ZERO || card.value == cardValues.ONE ||
        card.value == cardValues.TWO || card.value == cardValues.THREE ||
        card.value == cardValues.FOUR || card.value == cardValues.FIVE ||
        card.value == cardValues.SIX || card.value == cardValues.SEVEN ||
        card.value == cardValues.EIGHT || card.value == cardValues.NINE ||
        card.value == cardValues.SKIP || card.value == cardValues.REVERSE ||
        card.value == cardValues.DRAW_TWO || card.value == cardValues.WILD ||
        card.value == cardValues.WILD_DRAW_FOUR)
    })
  }
}
