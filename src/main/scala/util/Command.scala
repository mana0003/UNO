package util

import scala.util.{Try, Success, Failure}

trait Command {
  def execute(): Try[Unit]
  def undo(): Try[Unit]
  def redo(): Try[Unit]
}