package akka.first.app.mapreduce.actors

import akka.actor.{Actor, Props}
import akka.first.app.mapreduce.{MapData, ReduceData, Result}
import akka.routing.RoundRobinPool


class MasterActor extends Actor {

  val mapActor = context.actorOf(Props[MapActor].withRouter(RoundRobinPool(5)), "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(RoundRobinPool(5)), "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor], "aggregate")

  override def receive: Receive = {
    case line: String => mapActor ! line
    case mapData: MapData => reduceActor ! mapData
    case reduceData: ReduceData => aggregateActor ! reduceData
    case Result => aggregateActor forward Result
  }
}
