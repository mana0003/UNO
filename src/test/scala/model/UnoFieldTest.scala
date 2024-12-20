package model

import org.scalatest.funsuite.AnyFunSuite
import scala.model.*
import scala.view.*
import scala.util.*
import scala.controller.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class UnoFieldTest extends AnyWordSpec {
  "An UnoField" should {
    val field = UnoField()
    "have two players" in {
      field.players.size should be(2)
    }
  }
}