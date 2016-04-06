package akka.first.app.mapreduce.actors

import akka.actor.{Actor, Props}
import akka.first.app.mapreduce.{MapData, ReduceData, Result, WordCount}
import akka.routing.RoundRobinRouter

import scala.collection.mutable.ArrayBuffer


class MasterActor extends Actor {

  val mapActor = context.actorOf(Props[MapActor].withRouter(RoundRobinRouter(5)), "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(RoundRobinRouter(5)), "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor].withRouter(RoundRobinRouter(5)), "aggregate")

  override protected def receive: Receive = {
    case line: String => mapActor ! line
    case mapData: MapData => reduceActor ! mapData
    case reduceData: ReduceData => aggregateActor ! reduceData
    case Result => aggregateActor forward Result
  }
}
