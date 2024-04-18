package test
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike
import actors.Guardian
import messages.{GuardianMessage, QuoteRequest, Request}
import java.time.ZonedDateTime

class GuardianSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {
  "A Guardian actor" should {
    "send QuoteRequest messages to StockFetcher with correct parameters on receiving a Request message" in {
      val testTime = ZonedDateTime.now()
      val symbols = Seq("AAPL", "GOOGL")
      val guardian = spawn(Guardian(testTime, symbols, 10))

      val probe = createTestProbe[QuoteRequest]()
      guardian ! Request


    }
  }
}


