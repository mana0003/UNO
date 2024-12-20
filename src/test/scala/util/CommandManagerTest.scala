package util

import util.*
import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import scala.util.{Try, Success, Failure}

// Mock Command for Testing
class MockCommand(val name: String) extends Command {
  var executed = false
  var undone = false

  override def execute(): Try[Unit] = {
    if (executed) Failure(new IllegalStateException(s"Command $name already executed"))
    else {
      executed = true
      Success(())
    }
  }

  override def undo(): Try[Unit] = {
    if (!executed || undone) Failure(new IllegalStateException(s"Command $name cannot be undone"))
    else {
      undone = true
      executed = false
      Success(())
    }
  }
}

class CommandManagerTest extends AnyFunSuite with Matchers {

  test("CommandManager should execute a command and add it to the undo stack") {
    val manager = new CommandManager
    val command = new MockCommand("TestCommand")

    val result = manager.doStep(command)

    result shouldBe a[Success[_]]
    command.executed shouldBe true
  }

  test("CommandManager should undo a command and move it to the redo stack") {
    val manager = new CommandManager
    val command = new MockCommand("TestCommand")

    manager.doStep(command) shouldBe a[Success[_]]
    manager.undoStep shouldBe a[Success[_]]

    command.executed shouldBe false
    command.undone shouldBe true
  }

  test("CommandManager should redo a command and move it back to the undo stack") {
    val manager = new CommandManager
    val command = new MockCommand("TestCommand")

    manager.doStep(command) shouldBe a[Success[_]]
    manager.undoStep() shouldBe a[Success[_]]
    manager.redoStep() shouldBe a[Success[_]]

    command.executed shouldBe true
    command.undone shouldBe false
  }

  test("CommandManager should clear the redo stack after a new command is executed") {
    val manager = new CommandManager
    val command1 = new MockCommand("Command1")
    val command2 = new MockCommand("Command2")

    manager.doStep(command1) shouldBe a[Success[_]]
    manager.undoStep() shouldBe a[Success[_]]
    manager.redoStep() shouldBe a[Success[_]]

    // Execute a new command
    manager.doStep(command2) shouldBe a[Success[_]]

    // Attempting to redo should fail
    manager.redoStep() shouldBe a[Failure[_]]
  }

  test("CommandManager should return failure when undo stack is empty") {
    val manager = new CommandManager

    val result = manager.undoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[NoSuchElementException]
  }

  test("CommandManager should return failure when redo stack is empty") {
    val manager = new CommandManager

    val result = manager.redoStep()

    result shouldBe a[Failure[_]]
    result.failed.get shouldBe a[NoSuchElementException]
  }

  test("CommandManager should handle a failed command execution gracefully") {
    val manager = new CommandManager
    val failingCommand = new Command {
      override def execute(): Try[Unit] = Failure(new RuntimeException("Execution failed"))
      override def undo(): Try[Unit] = Failure(new RuntimeException("Undo failed"))
    }

    val result = manager.doStep(failingCommand)

    result shouldBe a[Failure[_]]
    manager.undoStep() shouldBe a[Failure[_]]
    manager.redoStep() shouldBe a[Failure[_]]
  }
}
