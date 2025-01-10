package model.gameComponent

import model.cardComponent.ICard
import model.gameComponent.IPlayer
import model.gameComponent.IPlayerHand
import scala.util.Try


trait IPlayer {
  def id: Int
  def hand: IPlayerHand
  def valid(card: ICard): Boolean
  def play(card: ICard): Try[IPlayer]
  def copy(hand: IPlayerHand): IPlayer
}

