package model
import scala.model.*

case class UnoField(
                     val players: List[Player] = (0 until 2).map(i => Player(i, PlayerHand())).toList,
                     val topCard: Card = randomCard,
                     val currentPlayer: Int = 0
                )