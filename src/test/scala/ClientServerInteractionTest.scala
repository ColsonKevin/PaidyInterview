import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Random, Success}

/***
 * Class to test the API
 */
class ClientServerInteractionTest extends FlatSpec {

  // Context initialisation
  private val thread: Thread = new Thread{override def run(): Unit = OrdersServer.main(null)}
  implicit val system: ActorSystem = ActorSystem("client-system")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit val items: Set[String] = Set("cake", "meat", "vegetables", "fruit", "water", "coke") // Set of items

  // Allows the server to start
  thread.start()
  Thread.sleep(2000)

  "The API's createOrder" should "work as expected" in {
    val table = (1 + Random.nextInt(3)).toString
    val item =  items.toVector(Random.nextInt(items.size))
    val map = Map("table" -> table , "item" -> item)
    val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()

    println(s"Order creation requested with parameters: $table; $item")
    val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/createOrder", entity = mapper))
    response.onComplete {
      case Success(result) => println(Unmarshal(result.entity).to[String])
        assert(Unmarshal(result.entity).to[String].equals("Order created"))
      case Failure(f) => println(f.getMessage)
    }
  }

  "The API's deleteOrder" should "work as expected" in {
    val table = (1 + Random.nextInt(3)).toString
    val item =  items.toVector(Random.nextInt(items.size))
    val map = Map("table" -> table, "item" ->  item)

    println(s"Order deletion requested with parameters: $table; $item")
    val mapper = new ObjectMapper().registerModule(DefaultScalaModule).writeValueAsString(map).getBytes()
    val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, "http://localhost:8080/deleteOrder", entity = mapper))
    response.onComplete {
      case Success(result) => println(Unmarshal(result.entity).to[String])
        assert(Unmarshal(result.entity).to[String].equals("Order delete"))
      case Failure(f) => println(f.getMessage)
    }
  }

  "The API's getOrders" should "work as expected" in {
    val table = (1 + Random.nextInt(3)).toString

    println(s"Orders list for a table requested with parameters: $table")
    val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
      "http://localhost:8080/getOrders?table=" +
        table))
    response.onComplete {
      case Success(result) => println(Unmarshal(result.entity).to[String])
        assert(Unmarshal(result.entity).to[String].equals("Orders not found"))
      case Failure(f) => println(f.getMessage)
    }
  }

  "The API's getOrder" should "work as expected" in {
    val table = (1 + Random.nextInt(3)).toString
    val item =  items.toVector(Random.nextInt(items.size))
    println(s"Specific order requested with parameters: $table; $item")
    val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =
      "http://localhost:8080/getOrder?table=" +
        table +
        "&item=" +
        item))
    response.onComplete {
      case Success(result) => println(Unmarshal(result.entity).to[String])
        assert(Unmarshal(result.entity).to[String].equals("Command not found"))
      case Failure(f) => println(f.getMessage)
    }
  }
}
