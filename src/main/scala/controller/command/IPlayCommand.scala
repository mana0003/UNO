package controller.command

trait IPlayCommand {
  def doStep(command: Command): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}