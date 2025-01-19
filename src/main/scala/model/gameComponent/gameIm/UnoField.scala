package model.gameComponent.gameIm

import model.cardComponent.ICard
import model.gameComponent.{IPlayerHand, IPlayer, IUnoField}
import com.google.inject.{AbstractModule, Guice, Inject}

case class UnoField @Inject() (
                                val players: List[IPlayer],
                                val topCard: ICard,
                                val currentPlayer: Int
                              ) extends IUnoField {
  def copy(
            players: List[IPlayer] = players,
            topCard: ICard = topCard,
            currentPlayer: Int = currentPlayer
          ): UnoField = {
    UnoField(players, topCard, currentPlayer)
  }
  def nextPlayer(): IUnoField = {
    copy(currentPlayer = (currentPlayer + 1) % players.length)
  }
  def toXml: scala.xml.Node = {
    <unoField>
      <players>
        {players.map(_.toXml)}
      </players>
      <topCard>
        {topCard.toXml}
      </topCard>
      <currentPlayer>
        {currentPlayer}
      </currentPlayer>
    </unoField>
  }

}