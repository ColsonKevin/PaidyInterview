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

/***
 * Runnable object respônsible for querying the api server
 */
object OrdersClient {

  /***
   * Main function
   * @param args: Array[String]
   */
  def main(args: Array[String]): Unit = {

    // Class values
    implicit val system: ActorSystem = ActorSystem("client-system")
    implicit val materializer: Materializer = Materializer(system)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    implicit val items: Set[String] = Set("cake", "meat", "vegetables", "fruit", "water", "coke") // Set of items
    implicit val currentTime: Long = System.currentTimeMillis() / 1000 // Used for the timer to live

    // Used to query multiple times
    while (true) {

      // Can query different cases at each loop randomly
      1 + Random.nextInt(4) match {

        // Post request to create an order. Parameters are randomized between boundaries to make the test relevant
        case 1 =>
          val map = Map("table" -> (1 + Random.nextInt(3)).toString, "item" ->  items.toVector(Random.nextInt(items.size)))
          val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/createOrder", entity = mapper))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }

        // Post request to delete an order
        case 2 =>
          val map = Map("table" -> (1 + Random.nextInt(3)).toString, "item" ->  items.toVector(Random.nextInt(items.size)))
          val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/deleteOrder", entity = mapper))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }

        // Get request to get all the orders of a specific table
        case 3 =>
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
            "http://localhost:8080/getOrders?table=" +
              (1 + Random.nextInt(3))))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }

        // get request to get a specific order of a specific table
        case 4 =>
          val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
            "http://localhost:8080/getOrder?table=" +
              (1 + Random.nextInt(3)) +
              "&item=" +
              items.toVector(Random.nextInt(items.size))))
          response.onComplete {
            case Success(result) => println(Unmarshal(result.entity).to[String])
            case Failure(f) => println(f.getMessage)
          }
      }

      // Slows the thread
      Thread.sleep(10000)

      // Terminate after 4 min
      if ((System.currentTimeMillis()/1000) - currentTime > 240) system.terminate()
    }
  }
}
