package akka.first.app.mapreduce.actors

import akka.actor.Actor
import akka.first.app.mapreduce.{MapData, ReduceData, WordCount}

import scala.collection.mutable.ArrayBuffer


class ReduceActor extends Actor {

  private def reduce(message: ArrayBuffer[WordCount]): ReduceData= {
    val map: Map[String, Int] = message.foldLeft(Map.empty[String, Int]) { (index, words) =>
      if (index.contains(words.word))
        index + (words.word -> (index.get(words.word).get + 1))
      else
        index + (words.word -> 1)
    }
    ReduceData(map)
  }

  override def receive: Receive = {
    case MapData(data) => sender ! reduce(data)
  }
}
