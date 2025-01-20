package model

import org.scalatest.funsuite.AnyFunSuite
import model.*
import view.*
import util.*
import controller.*
import model.gameComponent.gameIm.UnoField
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import model.gameComponent.IPlayer
import model.cardComponent.ICard
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.*
import scala.xml.Node

class UnoFieldTest extends AnyWordSpec {
  def createInitialField(): UnoField = {
    //val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    //val topCard = mock(classOf[ICard])
    //new UnoField(players, topCard, 0)
    val player1 = mock(classOf[IPlayer])
    val player2 = mock(classOf[IPlayer])
    val topCard = mock(classOf[ICard])

    // Mock the toXml method for IPlayer to return a simple XML node
    when(player1.toXml).thenReturn(<player>
      <name>Player 1</name>
    </player>)
    when(player2.toXml).thenReturn(<player>
      <name>Player 2</name>
    </player>)

    // Mock the toXml method for ICard to return a simple XML node
    when(topCard.toXml).thenReturn(<card>
      <color>Red</color> <value>5</value>
    </card>)

    new UnoField(List(player1, player2), topCard, 0)
  }
  "An UnoField" should {
    "have two players" in {
      val field = createInitialField()
      field.players.size should be(2)
    }

    "allow copying with new values" in {
      val field = createInitialField()
      val copiedField = field.copy(currentPlayer = 1)

      copiedField.currentPlayer should be(1)
      copiedField.players should be(field.players) // Same players list
      copiedField.topCard should be(field.topCard) // Same topCard
    }

    "switch to the next player" in {
      val field = createInitialField()

      field.currentPlayer should be(0)

      val nextField = field.nextPlayer()
      nextField.currentPlayer should be(1)

      val nextNextField = nextField.nextPlayer()
      nextNextField.currentPlayer should be(0)
    }

    "be serializable to XML" in {
      val field = createInitialField()

      val xml: Node = field.toXml
      (xml \ "players").size should be(2) // Should have two players in XML
      (xml \ "currentPlayer").text should be("0") // Check currentPlayer value

      (xml \ "topCard" \ "color").text should be("Red")
      (xml \ "topCard" \ "value").text should be("5")
    }
  }
}