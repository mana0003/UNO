package model.gameComponent

import model.cardComponent.cardIm.Card
import gameComponent.gameIm.PlayerHand

trait IPlayer {
  def id: Int
  def hand: PlayerHand
  def valid(card: Card): Boolean
  def play(card: Card): IPlayer
}

