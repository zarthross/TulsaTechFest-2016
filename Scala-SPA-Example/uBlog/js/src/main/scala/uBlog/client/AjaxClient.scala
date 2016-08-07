package uBlog.client

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future

import org.scalajs.dom
import upickle.Js
import upickle.default._

object AjaxClient extends autowire.Client[Js.Value, Reader, Writer]{
  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  def write[Result: Writer](r: Result) = writeJs(r)

  override def doCall(req: Request): Future[Js.Value] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(_.responseText)
      .map(upickle.json.read)
  }
}