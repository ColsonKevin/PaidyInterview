import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.stream.Materializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Random, Success}

object OrdersClient {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val items = Set("cake", "meat")//, "vegetables", "fruit", "water", "coke")
    val currentTime = System.currentTimeMillis() / 1000

    while (true) {
      1 + Random.nextInt(4) match {
        case 1 =>
          val map = Map("table" -> (1 + Random.nextInt(3)).toString, "item" ->  items.toVector(Random.nextInt(items.size)))
          val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/createCommand", entity = mapper))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }
        case 2 =>
          val map = Map("table" -> (1 + Random.nextInt(3)).toString, "item" ->  items.toVector(Random.nextInt(items.size)))
          val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/deleteCommand", entity = mapper))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }
        case 3 =>
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
            "http://localhost:8080/getCommands?table=" +
              (1 + Random.nextInt(3))))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }
        case 4 =>
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
            "http://localhost:8080/getCommand?table=" +
              (1 + Random.nextInt(3)) +
              "&item=" +
              items.toVector(Random.nextInt(items.size))))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }
      }
      Thread.sleep(10000)
      if ((System.currentTimeMillis()/1000) - currentTime > 300) system.terminate()
    }
  }
}
