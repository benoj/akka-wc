package akka.first.app.mapreduce.actors

import akka.actor.Actor
import akka.first.app.mapreduce.{MapData, ReduceData, Result, WordCount}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class AggregateActor extends Actor {

  val map = new mutable.HashMap[String, Int]

  private def aggregate(data: Map[String, Int]): Unit = {
    for((key,value) <- data) {
      if(map contains key){
        map(key) = value + map.get(key).get
      }
      else {
        map += (key -> value)
      }
    }
  }

  override protected def receive: Receive = {
    case ReduceData(data) => aggregate(data)
    case Result => sender ! map.toString
  }
}
