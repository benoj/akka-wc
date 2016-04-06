package akka.first.app.mapreduce.actors

import akka.actor.Actor
import akka.first.app.mapreduce.{MapData, WordCount}

import scala.collection.mutable.ArrayBuffer


class MapActor extends Actor {

  private val STOP_WORDS = List("a", "am", "an")

  private def evaluateExpression(message: String): MapData= {
    val words: ArrayBuffer[WordCount] = message.split("""\s+""").foldLeft(ArrayBuffer.empty[WordCount]) {
      (index, word) => if (!STOP_WORDS.contains(word.toLowerCase())) index += WordCount(word.toLowerCase, 1) else index
    }
    MapData(words)
  }

  override protected def receive: Receive = {
    case message: String => sender ! evaluateExpression(message)
  }
}
