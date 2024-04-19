package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import messages.CsvWriterProtocol

object CsvWriter {

  def apply(): Behavior[CsvWriterProtocol] = Behaviors.empty

}
