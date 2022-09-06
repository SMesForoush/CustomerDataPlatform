
object Main {
  def main(args: Array[String]): Unit = {
  println(args)
    val argument = args.map(_.toInt)
    val maxRecord =  argument(0) // set if you want get limit record -> -1
    val intervalRecord = argument(1) // set with ns  -> -1
    val intervalFlushRecord = argument(2) // set with ms -> 100
    println("Hello world!")
    println(intervalRecord)
  }
}