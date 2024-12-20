package model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import model.*
import view.*
import util.*
import controller.*

class PlayerTest extends AnyWordSpec {
  "Player" should {
    "play a card" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.RED, cardValues.ONE)
      player.play(card).isSuccess should be (true)
    }
    "check if card is valid" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.RED, cardValues.ONE)
      player.valid(card) should be (true)
    }
    "not play a card" in {
      val player = Player(0, PlayerHand(List(Card(cardColors.RED, cardValues.ONE))))
      val card = Card(cardColors.BLUE, cardValues.TWO)
      player.play(card).isFailure should be (true)
    }
  }
}