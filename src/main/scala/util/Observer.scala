package util
// refactoring.guru

trait Observable {
  private var subscribers: Vector[Observer] = Vector()

  def addObserver(observer: Observer): Unit = {
    subscribers = subscribers :+ observer
  }

  def removeObserver(observer: Observer): Unit = {
    subscribers = subscribers.filterNot(o => o == observer)
  }

  def notifyObservers(event: Event): Unit = {
    subscribers.foreach(o => o.update(event))
  }

  def getSubscribers: Vector[Observer] = subscribers
}

trait Observer {
  def update(e: Event): Unit
}

enum Event {
  case Start
  , Quit
  , Play
  , Draw
  , Undo
  , Redo
  , Error
  , Load
}

