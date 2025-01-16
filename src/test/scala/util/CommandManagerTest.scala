package util

//import util.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.util.{Try, Success, Failure}

class CommandManagerTest extends AnyFlatSpec with Matchers {

  trait TestCommand extends Command {
    var executed = false
    var undone = false
    var redone = false

    override def doStep(command: Command): Try[Unit] = {
      executed = true
      Success(())
    }

    override def undoStep(): Try[Unit] = {
      undone = true
      Success(())
    }

    override def redoStep(): Try[Unit] = {
      redone = true
      Success(())
    }
  }

  trait FailingCommand extends Command {
    override def doStep(command: Command): Try[Unit] = Failure(new Exception("Do step failed"))

    override def undoStep(): Try[Unit] = Failure(new Exception("Undo step failed"))

    override def redoStep(): Try[Unit] = Failure(new Exception("Redo step failed"))
  }

  "CommandManager" should "handle failure when executing a command" in {
    val commandManager = new CommandManager
    val command = new FailingCommand {}

    commandManager.doStep(command) shouldBe a[Failure[_]]
  }

  it should "handle failure when undoing a command" in {
    val commandManager = new CommandManager
    val command = new TestCommand {}

    commandManager.doStep(command)
    val failingCommand = new FailingCommand {}
    commandManager.pushToUndoStack(failingCommand)
    commandManager.undoStep() shouldBe a[Failure[_]]
  }

  it should "handle failure when redoing a command" in {
    val commandManager = new CommandManager
    val command = new TestCommand {}

    commandManager.doStep(command)
    commandManager.undoStep()
    val failingCommand = new FailingCommand {}
    commandManager.pushToRedoStack(failingCommand)
    commandManager.redoStep() shouldBe a[Failure[_]]
  }

  "CommandManager" should "execute a command successfully" in {
    val commandManager = new CommandManager
    val command = new TestCommand {}

    commandManager.doStep(command) shouldBe a[Success[_]]
    command.executed shouldBe true
  }

  it should "undo a command successfully" in {
    val commandManager = new CommandManager
    val command = new TestCommand {}

    commandManager.doStep(command)
    commandManager.undoStep() shouldBe a[Success[_]]
    command.undone shouldBe true
  }

  it should "redo a command successfully" in {
    val commandManager = new CommandManager
    val command = new TestCommand {}

    commandManager.doStep(command)
    commandManager.undoStep()
    commandManager.redoStep() shouldBe a[Success[_]]
    command.redone shouldBe true
  }

  it should "fail to undo when there are no commands to undo" in {
    val commandManager = new CommandManager

    //commandManager.undoStep() shouldBe a[Failure[_]]
    val result = commandManager.undoStep()
    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[NoSuchElementException]
    result.failed.get.getMessage shouldBe "No commands to undo"
  }

  it should "fail to redo when there are no commands to redo" in {
    val commandManager = new CommandManager

    //commandManager.redoStep() shouldBe a[Failure[_]]
    val result = commandManager.redoStep()
    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[NoSuchElementException]
    result.failed.get.getMessage shouldBe "No commands to redo"
  }

}