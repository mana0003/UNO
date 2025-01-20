package model
import model.cardComponent.cardIm.{Card, randomCards}
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.gameIm.PlayerHand
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import scala.xml.XML
import scala.xml.*

class PlayerHandTest extends AnyWordSpec {

  "PlayerHand" when {
    "created" should {
      "have 5 cards" in {
        val hand = PlayerHand()
        hand.cards.size should be(5)
      }

      "have random cards" in {
        val hand1 = PlayerHand()
        val hand2 = PlayerHand()
        hand1.cards should not be hand2.cards
      }

      "ensure that random cards are different on multiple hand creations" in {
        val hand1 = PlayerHand()
        val hand2 = PlayerHand()
        hand1.cards should not equal hand2.cards
      }
    }

    "existing" should {
      "be able to get a card removed" in {
        val hand = PlayerHand(randomCards(2))
        val card = hand.cards.head
        val newHand = hand.removeCard(card)
        newHand.cards should be(hand.cards.tail)
        newHand.cards.size should be(hand.cards.size - 1)
      }

      "be able to get a card added" in {
        val hand = PlayerHand(randomCards(2))
        val card = Card(cardColors.RED, cardValues.ZERO)
        val newHand = hand.addCard(card)
        newHand.cards should contain(card)
        newHand.cards.size should be(hand.cards.size + 1)
      }

      "not remove a card that doesn't exist" in {
        val hand = PlayerHand(randomCards(2))
        val nonExistentCard = Card(cardColors.GREEN, cardValues.ONE)
        val newHand = hand.removeCard(nonExistentCard)
        newHand.cards should be(hand.cards)
        newHand.cards.size should be(hand.cards.size)
      }

      "return an empty hand when all cards are removed" in {
        val hand = PlayerHand(randomCards(2))
        val cardToRemove = hand.cards.head
        val handAfterFirstRemove = hand.removeCard(cardToRemove)
        val handAfterSecondRemove = handAfterFirstRemove.removeCard(handAfterFirstRemove.cards.head)
        handAfterSecondRemove.cards shouldBe empty
      }

      "not fail when adding a card to an empty hand" in {
        val hand = PlayerHand(List())
        val card = Card(cardColors.RED, cardValues.ZERO)
        val newHand = hand.addCard(card)
        newHand.cards should contain(card)
        newHand.cards.size should be(1)
      }

      "not fail when removing a card from an empty hand" in {
        val hand = PlayerHand(List())
        val card = Card(cardColors.RED, cardValues.ZERO)
        val newHand = hand.removeCard(card)
        newHand.cards shouldBe empty
      }

      "properly serialize to XML" in {
        val cards = List(
          Card(cardColors.RED, cardValues.ZERO),
          Card(cardColors.BLUE, cardValues.ONE),
          Card(cardColors.GREEN, cardValues.TWO),
          Card(cardColors.YELLOW, cardValues.THREE),
          Card(cardColors.RED, cardValues.FOUR)
        )
        val hand = PlayerHand()
        val xml = hand.toXml

        (xml \ "playerHand").nonEmpty should be(true)
        (xml \ "playerHand" \ "card").size should be(5)

        val firstCardColor = (xml \ "playerHand" \ "card" \ "color").headOption.map(_.text) should be(Some("Red"))
        val firstCardValue = (xml \ "playerHand" \ "card" \ "value").headOption.map(_.text) should be(Some("0"))

        firstCardColor should be(Some("Red"))
        firstCardValue should be(Some("Zero"))
      }

    }
  }
}
