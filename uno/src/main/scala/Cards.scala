case class Card(color: String, value: String) {
  def isSpecial: Boolean = value match {
    case "Skip" | "Reverse" | "Draw Two" | "Wild" | "Wild Draw Four" => true
    case _ => false
  }
}