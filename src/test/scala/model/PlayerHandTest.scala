package model
import org.scalatest.funsuite.AnyFunSuite
import scala.model.*
import scala.view.*
import scala.util.*
import scala.controller.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*


class PlayerHandTest extends AnyWordSpec {
  "PlayerHand" when {
    "created" should {
      "have 5 cards" in {
        val hand = PlayerHand()
        hand.cards.size should be(5)
      }
      "which should be random" in {
        val hand1 = PlayerHand()
        val hand2 = PlayerHand()
        hand1.cards should not be hand2.cards
      }
    }
    "existing" should {
      "be able to get a card removed" in {
        val hand = PlayerHand(randomCards(2))
        val card = hand.cards.head
        val newHand = hand.removeCard(card)
        newHand.cards should be(hand.cards.tail)
      }
      "be able to get a card added" in {
        val hand = PlayerHand(randomCards(2))
        val card = Card(cardColors.RED, cardValues.ZERO)
        val newHand = hand.addCard(card)
        newHand.cards should be(card +: hand.cards)
      }
    }
  }
}