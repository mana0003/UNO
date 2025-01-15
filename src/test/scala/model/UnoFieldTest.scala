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

class UnoFieldTest extends AnyWordSpec {
  def createInitialField(): UnoField = {
    val players = List(mock(classOf[IPlayer]), mock(classOf[IPlayer]))
    val topCard = mock(classOf[ICard])
    new UnoField(players, topCard, 0)
  }
  "An UnoField" should {
    val field = createInitialField()
    "have two players" in {
      field.players.size should be(2)
    }
  }
}