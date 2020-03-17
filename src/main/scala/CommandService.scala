import java.util
import java.util.stream.Collectors

class CommandService {
    private var commands = Map.empty[Int, util.ArrayList[Command]]

    def create_command(table: Int, command: Command): Unit = {
        commands.get(table) match {
            case Some(x) => x.add(command)
            case None =>
                val commandList = new util.ArrayList[Command]()
                commandList.add(command)
                commands = commands + (table -> commandList)
        }
    }

    def get_commands(table: Int): util.ArrayList[Command] = {
        commands.get(table) match {
            case Some(x) => x
            case None => new util.ArrayList[Command]()
        }
    }

    def get_command_specific(table: Int, item: String): util.ArrayList[Command] = {
        commands.get(table) match {
            case None => new util.ArrayList[Command]()
            case Some(x) => val list = x.stream().filter(_.get_item().equals(item))
              .collect(Collectors.toList[Command])
                new util.ArrayList[Command](list)
        }
    }

    def delete_command(table: Int, item: String): Unit = {
        commands.get(table) match {
            case Some(x) => x.removeIf(_.equals(item))
            case None =>
        }
    }
}