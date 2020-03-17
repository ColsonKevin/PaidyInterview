import java.util
import java.util.stream.Collectors

import scala.collection.mutable
import scala.collection.mutable.Map

class CommandService {
    var commands: mutable.Map[Int, util.ArrayList[Command]]

    def create_command(table: Int, command: Command): Unit = {
        commands.get(table) match {
            case Some(x) => x.add(command)
            case None => {
                val commandList = new util.ArrayList[Command]()
                commandList.add(command)
                commands.addOne((table, commandList))
            }
        }
    }

    def get_commands(table: Int): util.ArrayList[Command] = {
        commands.get(table) match {
            case Some(x) => x
            case None => new util.ArrayList[Command]()
        }
    }

    def get_command_specific(table: Int, command: Command): util.ArrayList[Command] = {
        commands.get(table) match {
            case None => new util.ArrayList[Command]()
            case Some(x) => val list = x.stream().filter(_.get_item().equals(command.get_item()))
              .collect(Collectors.toList[Command])
                new util.ArrayList[Command](list)
        }
    }

    def delete_command(table: Int, command: Command): Unit = {
        commands.get(table) match {
            case Some(x) => x.remove(command)
            case None =>
        }
    }
}
