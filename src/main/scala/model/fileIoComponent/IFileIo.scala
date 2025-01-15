package model.fileIoComponent

import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}

trait IFileIo {
  def load: IUnoField
  def save(unoField: IUnoField, players: List[IPlayer], playerHands: List[IPlayerHand]): Unit
}