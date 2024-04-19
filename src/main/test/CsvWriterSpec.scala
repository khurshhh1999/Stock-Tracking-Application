package test

import actors.CsvWriter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import scala.io.Source


import java.nio.file.{Files, Paths}

class CsvWriterSpec extends AnyFlatSpec with Matchers {
  val testKit = ActorTestKit()

  "CsvWriter" should "initialize with the correct file name and create a file" in {
    val actor = testKit.spawn(CsvWriter("testFile"))
    val path = Paths.get("data/testFile.csv")
    Files.exists(path) shouldBe true
    Files.deleteIfExists(path)  // Clean up after test
  }

  it should "write headers correctly to the CSV file" in {
    val actor = testKit.spawn(CsvWriter("testFile"))
    val path = Paths.get("data/testFile.csv")
    val source = Source.fromFile(path.toFile)
    val headers = source.getLines().next()
    headers shouldEqual "symbol,timestamp,currentPrice,rd,min,max,avg,macd,rsi,bollingerBands,ema,adx,stochK,stochD,volume,williamsR,atr,cci"
    source.close()
    Files.deleteIfExists(path)  // Clean up after test
  }

  // Additional test cases for data writing, concurrency, and error handling can be added here.
}


