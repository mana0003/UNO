package util

import scala.util.Try

trait Command {
  def doStep(): Try[Unit]
  def undoStep(): Try[Unit]
  def redoStep(): Try[Unit]
}