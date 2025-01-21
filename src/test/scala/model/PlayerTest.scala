package model

import model.cardComponent.ICard
import model.cardComponent.cardIm.Card
import model.gameComponent.{IPlayer, IPlayerHand}
import model.gameComponent.gameIm.Player
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import scala.util.{Failure, Success}
import scala.xml.Node

class PlayerTest extends AnyFlatSpec with Matchers with MockitoSugar {

  "Player" should "create a copy with a new hand" in {
    val mockHand = mock[IPlayerHand]
    val player = Player(1, mockHand)

    val newHand = mock[IPlayerHand]
    val copiedPlayer = player.copy(newHand)

    copiedPlayer.hand shouldEqual newHand
    copiedPlayer.id shouldEqual player.id
  }

  it should "return true if the card can be played" in {
    val mockHand = mock[IPlayerHand]
    val mockCard = mock[ICard]
    val player = Player(1, mockHand)

    when(mockHand.cards).thenReturn(List(mockCard))
    when(mockCard.canBePlayed(mockCard)).thenReturn(true)

    player.valid(mockCard) shouldEqual true
  }

  it should "return false if the card cannot be played" in {
    val mockHand = mock[IPlayerHand]
    val mockCard = mock[ICard]
    val player = Player(1, mockHand)

    when(mockHand.cards).thenReturn(List(mockCard))
    when(mockCard.canBePlayed(mockCard)).thenReturn(false)

    player.valid(mockCard) shouldEqual false
  }

  it should "successfully play a valid card" in {
    val mockHand = mock[IPlayerHand]
    val mockCard = mock[ICard]
    val player = Player(1, mockHand)

    when(mockHand.cards).thenReturn(List(mockCard))
    when(mockCard.canBePlayed(mockCard)).thenReturn(true)
    when(mockHand.removeCard(mockCard)).thenReturn(mockHand)

    player.play(mockCard) shouldEqual Success(Player(1, mockHand))
  }

  it should "fail to play an invalid card" in {
    val mockHand = mock[IPlayerHand]
    val mockCard = mock[ICard]
    val player = Player(1, mockHand)

    when(mockHand.cards).thenReturn(List(mockCard))
    when(mockCard.canBePlayed(mockCard)).thenReturn(false)

    val actual = player.play(mockCard)

    actual match {
      case Failure(e) =>
        // Print the actual exception and message for debugging purposes
        println(s"Actual failure message: ${e.getMessage}")
        println(s"Actual failure type: ${e.getClass.getName}")

        // Check if the exception message is what we expect
        e.getMessage shouldEqual "Illegal move."

        // Check if the exception type is the expected one
        e shouldBe a[IllegalArgumentException]

      case _ =>
        fail("Expected Failure")
    }

    //player.play(mockCard) shouldEqual Failure(new IllegalArgumentException("Illegal move."))
  }

  it should "serialize to XML correctly" in {
    val mockHand = mock[IPlayerHand]
    val player = Player(1, mockHand)

    when(mockHand.toXml).thenReturn(<hand><card>Card1</card></hand>)

    val xml: Node = player.toXml

    xml.toString() should include("<player>")
    xml.toString() should include("<id>1</id>")
    xml.toString() should include("<hand>")
    xml.toString() should include("<card>Card1</card>")
  }
}
