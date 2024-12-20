package util

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite

class TestObserver extends Observer {
  var receivedEvents: List[Event] = List()

  override def update(e: Event): Unit = {
    receivedEvents = receivedEvents :+ e
  }
}

class ObservableTest extends AnyFunSuite with Matchers {

  // Test observable implementation
  class TestObservable extends Observable

  test("Observable should allow adding observers and notifying them") {
    val observable = new TestObservable
    val observer1 = new TestObserver
    val observer2 = new TestObserver

    // Add observers
    observable.addObserver(observer1)
    observable.addObserver(observer2)

    // Notify observers
    observable.notifyObservers(Event.Start)

    // Both observers should have received the Event.Start
    observer1.receivedEvents should contain theSameElementsAs List(Event.Start)
    observer2.receivedEvents should contain theSameElementsAs List(Event.Start)
  }

  test("Observable should allow removing observers") {
    val observable = new TestObservable
    val observer1 = new TestObserver
    val observer2 = new TestObserver

    // Add observers
    observable.addObserver(observer1)
    observable.addObserver(observer2)

    // Remove one observer
    observable.removeObserver(observer1)

    // Notify observers
    observable.notifyObservers(Event.Quit)

    // Only observer2 should have received the Event.Quit
    observer1.receivedEvents should be(empty)
    observer2.receivedEvents should contain theSameElementsAs List(Event.Quit)
  }

  test("Observable should notify all observers even with multiple events") {
    val observable = new TestObservable
    val observer1 = new TestObserver
    val observer2 = new TestObserver

    // Add observers
    observable.addObserver(observer1)
    observable.addObserver(observer2)

    // Notify multiple events
    observable.notifyObservers(Event.Play)
    observable.notifyObservers(Event.Draw)

    // Both observers should have received both events in order
    observer1.receivedEvents should contain theSameElementsInOrderAs List(Event.Play, Event.Draw)
    observer2.receivedEvents should contain theSameElementsInOrderAs List(Event.Play, Event.Draw)
  }

  test("Observable should handle empty observer list gracefully") {
    val observable = new TestObservable

    // Notify without any observers
    noException should be thrownBy {
      observable.notifyObservers(Event.Undo)
    }
  }
}