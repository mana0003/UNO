package model

import model.cardComponent.ICard
import model.gameComponent.{IPlayer, IUnoField}
import model.gameComponent.gameIm.UnoField
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class UnoFieldTest extends AnyFlatSpec with Matchers with MockitoSugar {

  "UnoField" should "create a copy with the same attributes when no arguments are passed" in {
    val mockPlayer1 = mock[IPlayer]
    val mockPlayer2 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1, mockPlayer2), mockTopCard, 0)

    val copiedField = unoField.copy()

    copiedField.players shouldEqual unoField.players
    copiedField.topCard shouldEqual unoField.topCard
    copiedField.currentPlayer shouldEqual unoField.currentPlayer
  }

  it should "create a copy with updated players when players are passed" in {
    val mockPlayer1 = mock[IPlayer]
    val mockPlayer2 = mock[IPlayer]
    val mockPlayer3 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1, mockPlayer2), mockTopCard, 0)

    val copiedField = unoField.copy(players = List(mockPlayer1, mockPlayer3))

    copiedField.players shouldEqual List(mockPlayer1, mockPlayer3)
  }

  it should "create a copy with updated topCard when topCard is passed" in {
    val mockPlayer1 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val newTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1), mockTopCard, 0)

    val copiedField = unoField.copy(topCard = newTopCard)

    copiedField.topCard shouldEqual newTopCard
  }

  it should "create a copy with updated currentPlayer when currentPlayer is passed" in {
    val mockPlayer1 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1), mockTopCard, 0)

    val copiedField = unoField.copy(currentPlayer = 1)

    copiedField.currentPlayer shouldEqual 1
  }

  it should "increment currentPlayer correctly in nextPlayer" in {
    val mockPlayer1 = mock[IPlayer]
    val mockPlayer2 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1, mockPlayer2), mockTopCard, 0)

    val nextField = unoField.nextPlayer()

    nextField.currentPlayer shouldEqual 1
  }

  it should "wrap around currentPlayer correctly in nextPlayer" in {
    val mockPlayer1 = mock[IPlayer]
    val mockPlayer2 = mock[IPlayer]
    val mockTopCard = mock[ICard]
    val unoField = UnoField(List(mockPlayer1, mockPlayer2), mockTopCard, 1)

    val nextField = unoField.nextPlayer()

    nextField.currentPlayer shouldEqual 0
  }

  it should "serialize to XML correctly" in {
    val mockPlayer1 = mock[IPlayer]
    when(mockPlayer1.toXml).thenReturn(<player>Player1</player>)

    val mockTopCard = mock[ICard]
    when(mockTopCard.toXml).thenReturn(<card>TopCard</card>)

    val unoField = UnoField(List(mockPlayer1), mockTopCard, 0)

    val xml = unoField.toXml

    //xml.toString() shouldEqual
      //"""<unoField><players><player>Player1</player></players><topCard><card>TopCard</card></topCard><currentPlayer>0</currentPlayer></unoField>"""
    val expectedXml = """<unoField><players><player>Player1</player></players><topCard><card>TopCard</card></topCard><currentPlayer>0</currentPlayer></unoField>"""
    val normalizedExpected = expectedXml.replaceAll("\\s+", "")
    val normalizedActual = xml.toString().replaceAll("\\s+", "")

    normalizedExpected shouldEqual normalizedActual
  }
}
