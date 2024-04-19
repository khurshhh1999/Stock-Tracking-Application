package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import messages.RecordKeeperProtocol

object RecordKeeper {

  def apply():Behavior[RecordKeeperProtocol] = Behaviors.empty


}
