package model

import model.cardComponent.cardIm.{Card, randomCard, randomCards}
import model.cardComponent.ICard
import model.cardComponent.{cardColors, cardValues}
import org.scalatest.funsuite.AnyFunSuite
//import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import model.*
//import view.*
//import util.*
//import scala.io.AnsiColor

import scalafx.scene.paint.Color

class CardsTest extends AnyFunSuite {

  // Test case for the canBePlayed method
  test("canBePlayed should return true if the color or value matches or if it's a Wild card") {
    val redCard = Card(cardColors.RED, cardValues.THREE)
    val blueCard = Card(cardColors.BLUE, cardValues.THREE)
    val greenCard = Card(cardColors.GREEN, cardValues.FIVE)
    val wildCard = Card(cardColors.RED, cardValues.WILD)

    assert(redCard.canBePlayed(blueCard)) // Same value
    assert(blueCard.canBePlayed(redCard)) // Same value
    //assert(greenCard.canBePlayed(redCard)) // Different color and value
    assert(wildCard.canBePlayed(redCard)) // Wild card can always be played
    assert(redCard.canBePlayed(wildCard)) // Wild card can always be played
  }

  // Test case for the getColorCode method
  test("getColorCode should return correct color based on the card color or wild type") {
    val redCard = Card(cardColors.RED, cardValues.THREE)
    val greenCard = Card(cardColors.GREEN, cardValues.SKIP)
    val yellowCard = Card(cardColors.YELLOW, cardValues.DRAW_TWO)
    val blueCard = Card(cardColors.BLUE, cardValues.ZERO)
    val wildCard = Card(cardColors.BLACK, cardValues.WILD)
    val wildDrawFourCard = Card(cardColors.BLACK, cardValues.WILD_DRAW_FOUR)

    assert(redCard.getColorCode == Color.Red)
    assert(greenCard.getColorCode == Color.Green)
    assert(yellowCard.getColorCode == Color.Yellow)
    assert(blueCard.getColorCode == Color.Blue)
    assert(wildCard.getColorCode == Color.Black)
    assert(wildDrawFourCard.getColorCode == Color.Black)
  }

  test("getColorCode should handle null color gracefully") {
    val nullCard = new Card(null.asInstanceOf[cardColors], cardValues.THREE)
    assert(nullCard.getColorCode == Color.Black)
  }

  test("copy should create a new card with the same value and new color") {
    val originalCard = Card(cardColors.RED, cardValues.THREE)
    val copiedCard = originalCard.copy(cardColors.GREEN)
    assert(copiedCard.getColor == cardColors.GREEN)
    assert(copiedCard.getValue == originalCard.getValue)
  }

  // Test case for randomCard generation
  test("randomCard should return a Card with valid color and value") {
    val card = randomCard
    assert(card.getColor == cardColors.RED || card.getColor == cardColors.GREEN ||
      card.getColor == cardColors.YELLOW || card.getColor == cardColors.BLUE
      || card.getColor == cardColors.BLACK)
    assert(card.getValue == cardValues.ZERO || card.getValue == cardValues.ONE ||
      card.getValue == cardValues.TWO || card.getValue == cardValues.THREE ||
      card.getValue == cardValues.FOUR || card.getValue == cardValues.FIVE ||
      card.getValue == cardValues.SIX || card.getValue == cardValues.SEVEN ||
      card.getValue == cardValues.EIGHT || card.getValue == cardValues.NINE ||
      card.getValue == cardValues.SKIP || card.getValue == cardValues.DRAW_TWO ||
      card.getValue == cardValues.WILD || card.getValue == cardValues.WILD_DRAW_FOUR)
  }

  // Test case for randomCards generation
  test("randomCards should return a list of random Cards with valid colors and values") {
    val cards = randomCards(5)
    assert(cards.length == 5)
    cards.foreach(card => {
      assert(card.getColor == cardColors.RED || card.getColor == cardColors.GREEN ||
        card.getColor == cardColors.YELLOW || card.getColor == cardColors.BLUE
        || card.getColor == cardColors.BLACK)
      assert(card.getValue == cardValues.ZERO || card.getValue == cardValues.ONE ||
        card.getValue == cardValues.TWO || card.getValue == cardValues.THREE ||
        card.getValue == cardValues.FOUR || card.getValue == cardValues.FIVE ||
        card.getValue == cardValues.SIX || card.getValue == cardValues.SEVEN ||
        card.getValue == cardValues.EIGHT || card.getValue == cardValues.NINE ||
        card.getValue == cardValues.SKIP || card.getValue == cardValues.DRAW_TWO ||
        card.getValue == cardValues.WILD || card.getValue == cardValues.WILD_DRAW_FOUR)
    })
  }
  test("randomCards should return an empty list when i is 0") {
    val cards = randomCards(0)
    assert(cards.isEmpty)
  }

  test("randomCards should return a list of valid cards with the correct length") {
    val cards = randomCards(5)
    assert(cards.length == 5)
    cards.foreach(card => {
      assert(card.getColor != null)
      assert(card.getValue != null)
    })
  }
}
