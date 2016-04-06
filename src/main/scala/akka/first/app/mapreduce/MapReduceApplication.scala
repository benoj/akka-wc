package akka.first.app.mapreduce

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.first.app.mapreduce.actors.MasterActor
import akka.util.Timeout
import akka.pattern.ask

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}
import scala.util.Success



sealed trait MapReduceMessage

case class WordCount(word: String, count: Int) extends MapReduceMessage

case class MapData(dataList: ArrayBuffer[WordCount]) extends MapReduceMessage

case class ReduceData(data: Map[String, Int]) extends MapReduceMessage

case class Result() extends MapReduceMessage

object MapReduceApplication extends App {
  private val system = ActorSystem("MapReduceApp")
  private val master = system.actorOf(Props[MasterActor], "master")
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)


  master ! "the quick brown fox tried to jump over the lazy dog and fell on the dog"
  master ! "Dog is a man's best friend"
  master ! "Dog and Fox belong to the same family"

  Thread.sleep(500)
  val future: Future[String] = (master ? Result).mapTo[String]

  val result = Await.ready(future, timeout.duration).value.get

  println(result)
  
  system.terminate()
}