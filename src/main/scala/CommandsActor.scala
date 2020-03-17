import akka.actor.Actor

import scala.util.Random

case class GetCommandsMessage(table: Int)
case class CreateCommandMessage(table: Int, item: String)
case class CommandCreatedMessage(action: String)

class CommandsActor extends Actor {

  implicit val commandService: CommandService = new CommandService

  def receive: Receive = {
    case message: GetCommandsMessage => sender() ! commandService.get_commands(message.table)
    case message: CreateCommandMessage =>
      val command: Command = new Command
      val rand: Random = Random
      command.set_item(message.item)
      command.set_cook_time(5 + rand.nextInt(10))
      commandService.create_command(message.table, command)
      sender() ! "Command created"
  }
}