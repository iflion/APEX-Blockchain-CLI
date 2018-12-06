package com.apex.cli

class SysCommand extends CompositeCommand {
  override val cmd: String = "sys"
  override val description: String = "Command Line Interface to the system, omit it and type the sub command directly is legal."

  override val subCommands: Seq[Command] = Seq(
    new VersionC
  )
}

class HelpC extends Command {
  override val cmd: String = "help"
  override val description: String = "help"
  override val sys: Boolean = true

  override def execute(params: List[String]): Result = {

    Help(Command.helpMessage(Command.all))
  }
}

class VersionC extends VerC {
  override val cmd = "version"
  override val sys: Boolean = false
}

class VerC extends Command {
  override val cmd = "ver"
  override val description = "Version information"
  override val sys: Boolean = true

  override def execute(params: List[String]): Result = {Success("1")}
}

class ExitC extends Command {
  override val cmd = "exit"
  override val description = "exit"
  override val sys: Boolean = true

  override def execute(params: List[String]): Result = new Quit
}

class ClearC extends Command {
  override val cmd = "clear"
  override val description = "Clear characters on screen"
  override val sys: Boolean = true

  override def execute(params: List[String]): Result = {

    /*try {
      val reader = new ConsoleReader()
      val mask = '0';
      val s =  reader.readLine("s>", mask)
      println(s)
    }catch{
      case e: Exception => Error(e)
    }*/

    /* val reader = LineReaderBuilder.builder().build()
     val prompt = "aaaa"
     while (true) {
       var line = ""
       try {
         line = reader.readLine(prompt, '*')
         println(line)
       } catch{
         case e: UserInterruptException => Error(e)
         case e: EndOfFileException => Error(e)

       }
     }*/

    Success("clear success\n")
  }
}
