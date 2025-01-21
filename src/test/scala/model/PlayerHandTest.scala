package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues, ICard}
import model.gameComponent.gameIm.PlayerHand

class PlayerHandTest extends AnyWordSpec with Matchers {

  "A PlayerHand" should {

    "initialize with default random cards when no cards are provided" in {
      val playerHand = PlayerHand()
      playerHand.cards should have size 5
    }

    "allow adding a card" in {
      val cardToAdd: ICard = Card(cardColors.RED, cardValues.FIVE)
      val playerHand = PlayerHand(List(Card(cardColors.BLUE, cardValues.TWO)))
      val updatedHand = playerHand.addCard(cardToAdd)
      updatedHand.cards should contain(cardToAdd)
      updatedHand.cards should have size 2
    }

    "allow removing a card that exists in the hand" in {
      val cardToRemove: ICard = Card(cardColors.YELLOW, cardValues.NINE)
      val playerHand = PlayerHand(List(cardToRemove, Card(cardColors.GREEN, cardValues.SEVEN)))
      val updatedHand = playerHand.removeCard(cardToRemove)
      updatedHand.cards should not contain cardToRemove
      updatedHand.cards should have size 1
    }

    "do nothing when removing a card that does not exist" in {
      val existingCard: ICard = Card(cardColors.BLUE, cardValues.TWO)
      val nonExistingCard: ICard = Card(cardColors.RED, cardValues.THREE)
      val playerHand = PlayerHand(List(existingCard))
      val updatedHand = playerHand.removeCard(nonExistingCard)
      updatedHand.cards should contain(existingCard)
      updatedHand.cards should have size 1
    }

    "serialize to XML correctly" in {
      val card1: ICard = Card(cardColors.GREEN, cardValues.FOUR)
      val card2: ICard = Card(cardColors.RED, cardValues.EIGHT)
      val playerHand = PlayerHand(List(card1, card2))
      val xml = playerHand.toXml
      xml.label should be ("playerHand")
      (xml \ "card").size should be (2)
    }
  }
}
