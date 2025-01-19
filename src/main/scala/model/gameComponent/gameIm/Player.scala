package model.gameComponent.gameIm

import com.google.inject.{AbstractModule, Guice, Inject}
import model.cardComponent.ICard
import model.cardComponent.cardIm.Card
import model.gameComponent.{IPlayer, IPlayerHand}

import scala.util.{Failure, Success, Try}
import scala.xml.Node

case class Player @Inject() (id: Int, hand: IPlayerHand) extends IPlayer {

  def copy(hand: IPlayerHand): IPlayer = {
    Player(id, hand)
  }

  def valid(card: ICard): Boolean = {
    hand.cards.exists(_.canBePlayed(card))
  }

  def play(card: ICard): Try[IPlayer] = {
    if (valid(card)) {
      Success(Player(id, hand.removeCard(card)))
    } else {
      Failure(new IllegalArgumentException("Illegal move."))
    }
  }

  override def toXml: Node = {
    <player>
      <id>
        {id}
      </id>
      <hand>
        {hand.toXml}
      </hand>
    </player>
  }
}