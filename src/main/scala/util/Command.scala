package util

import scala.util.Try

trait Command {
  def doStep(command: Command): Try[Unit]
  def undoStep(): Try[Unit]
  def redoStep(): Try[Unit]
}