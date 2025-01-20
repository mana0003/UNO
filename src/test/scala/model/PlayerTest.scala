package model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import model.*
import view.*
import util.*
import controller.*
import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues}
import scala.util.{Failure, Success}
import org.mockito.Mockito._
import model.gameComponent.gameIm
import model.gameComponent.IPlayerHand
import model.gameComponent.gameIm.{Player, PlayerHand}

class PlayerTest extends AnyWordSpec {
  "Player" should {
    "be created with a hand of cards" in {
      val mockHand = mock(classOf[IPlayerHand])
      when(mockHand.cards).thenReturn(List(Card(cardColors.RED, cardValues.ONE)))
      val player = Player(0, mockHand)

      player.id should be(0)
      player.hand.cards should contain(Card(cardColors.RED, cardValues.ONE))
    }
    "copy a player with a new hand" in {
      val hand1 = PlayerHand(List(Card(cardColors.RED, cardValues.ONE)))
      val player1 = Player(0, hand1)
      val hand2 = PlayerHand(List(Card(cardColors.BLUE, cardValues.TWO)))
      val player2 = player1.copy(hand2)

      player2.id should be(0)
      player2.hand.cards should contain(Card(cardColors.BLUE, cardValues.TWO))
    }
    "validate a card" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val validCard = Card(cardColors.RED, cardValues.ONE)
      val invalidCard = Card(cardColors.BLUE, cardValues.TWO)

      player.valid(validCard) should be(true) // Valid card
      player.valid(invalidCard) should be(false) // Invalid card
    }
    "play a valid card" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val validCard = Card(cardColors.RED, cardValues.ONE)

      player.play(validCard) match {
        case Success(p) => p.hand.cards should not contain validCard
        case Failure(_) => fail("Card play should succeed")
      }
    }

    "fail to play an invalid card" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val invalidCard = Card(cardColors.BLUE, cardValues.TWO)

      player.play(invalidCard) match {
        case Success(_) => fail("Card play should fail")
        case Failure(ex) => ex shouldBe a[IllegalArgumentException]
      }
    }

    "serialize to XML" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val xml = player.toXml

      (xml \ "player" \ "id").text should be("0")
      //(xml \ "player" \ "hand").nonEmpty should be(true)

      (xml \ "player" \ "hand" \ "card").size should be(1)
      (xml \ "player" \ "hand" \ "card" \ "color").text should be("Red")
      (xml \ "player" \ "hand" \ "card" \ "value").text should be("1")
    }

    /*"play a card" in {
      val player = gameIm.Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.RED, cardValues.ONE)
      player.play(card).isSuccess should be (true)
    }
    "check if card is valid" in {
      val player = gameIm.Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.RED, cardValues.ONE)
      player.valid(card) should be (true)
    }
    "not play a card" in {
      val player = gameIm.Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.BLUE, cardValues.TWO)
      player.play(card).isFailure should be (true)
    }*/
  }
}