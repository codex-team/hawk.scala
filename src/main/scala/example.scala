import codex.hawk.HawkCatcher

object example extends App {

  try {
    throw new Exception("Major error")
  } catch {
    case e: Exception => new HawkCatcher("69d86244-f792-47ad-8e9a-23fee358e062", "http://localhost:3000/catcher/scala").catchException(e)
  }

}
