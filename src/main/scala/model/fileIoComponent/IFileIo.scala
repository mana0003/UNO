package model.fileIoComponent

import model.gameComponent.IUnoField

trait IFileIo {
  def load: IUnoField
  def save(unoField: IUnoField): Unit
}
