package util

import util.*
import util.Observable
import org.scalatest._
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

    observable.getSubscribers.size should be(2)

    // Verify the internal subscribers Vector is modified
    //assert(observable.subscribers.size == 2)

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
    observable.getSubscribers.size should be(2)
    // Verify the internal subscribers Vector is modified
    //assert(observable.subscribers.size == 2)

    // Remove one observer
    observable.removeObserver(observer1)
    observable.getSubscribers.size should be(1)

    // Verify the internal subscribers Vector is modified after removal
    //assert(observable.subscribers.size == 1)

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

    // Verify the internal subscribers Vector is modified
    observable.getSubscribers.size should be(2)
    //assert(observable.subscribers.size == 2)

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

    observable.getSubscribers.size should be(0)
    
    // Verify the internal subscribers Vector is still empty
    //assert(observable.subscribers.isEmpty)
  }

  test("Observable should handle removing all observers correctly") {
    val observable = new TestObservable
    val observer1 = new TestObserver
    val observer2 = new TestObserver

    // Add observers
    observable.addObserver(observer1)
    observable.addObserver(observer2)

    // Verify the internal subscribers Vector is modified
    observable.getSubscribers.size should be(2)

    //assert(observable.subscribers.size == 2)

    // Remove all observers
    observable.removeObserver(observer1)
    observable.removeObserver(observer2)

    // Verify the internal subscribers Vector is now empty
    observable.getSubscribers.size should be(0)

    //assert(observable.subscribers.isEmpty)

    // Notify observers (should not notify anyone)
    observable.notifyObservers(Event.Redo)

    // No observers should have received the event
    observer1.receivedEvents should be(empty)
    observer2.receivedEvents should be(empty)
  }

  test("Observable should correctly manage Vector of observers") {
    val observable = new TestObservable
    val observer1 = new TestObserver
    val observer2 = new TestObserver
    val observer3 = new TestObserver

    // Add observers to the observable
    observable.addObserver(observer1)
    observable.addObserver(observer2)
    observable.addObserver(observer3)

    // Verify the internal subscribers Vector is modified
    observable.getSubscribers.size should be(3)

    //assert(observable.subscribers.size == 3)

    // Notify observers, all should receive the event
    observable.notifyObservers(Event.Start)
    observer1.receivedEvents should contain theSameElementsAs List(Event.Start)
    observer2.receivedEvents should contain theSameElementsAs List(Event.Start)
    observer3.receivedEvents should contain theSameElementsAs List(Event.Start)

    // Remove one observer
    observable.removeObserver(observer2)


    observable.getSubscribers.size should be(2)
    // Verify the internal subscribers Vector is modified after removal
    //assert(observable.subscribers.size == 2)

    // Notify again, only two observers should receive the event
    observable.notifyObservers(Event.Quit)
    observer1.receivedEvents should contain theSameElementsAs List(Event.Start, Event.Quit)
    observer2.receivedEvents should have size 1 // It only received Event.Start before removal
    observer3.receivedEvents should contain theSameElementsAs List(Event.Start, Event.Quit)
  }
}
