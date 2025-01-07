package controller.command

trait IDrawCommand {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}