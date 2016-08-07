package uBlog.server

import uBlog.shared._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import upickle.default._
import upickle.Js

object WebServer extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val route =
    get{
      pathSingleSlash {
        complete {
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            Template.txt.toString
          )
        }
      } ~
      path("js" / "ublogclient-" ~ Remaining) { (r: String) =>
        getFromResource("ublogclient-" + r)
      } ~
      pathPrefix("stylesheets") {
        getFromResourceDirectory("stylesheets")
      } ~
      pathPrefix("webjars") {
        WebJars.apply
      }
    } ~
    post {
      path("api" / Segments) { s =>
        entity(as[String]) { e =>
          complete {
            AutowireServer.route[BlogApi](BlogApiImpl)(
              autowire.Core.Request(
                s,
                upickle.json.read(e).asInstanceOf[Js.Obj].value.toMap
              )
            ).map(upickle.json.write(_))
          }
        }
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)
}

object Template {
  import scalatags.Text.all._
  import scalatags.Text.tags2.title

  val txt =
    "<!DOCTYPE html>" +
    html(
      head(
        title("uBlog"),
        meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="/webjars/bootstrap/css/bootstrap.css"
        ),
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="/stylesheets/blog.css"
        )
      ),
      body(margin:=0)(
        div(id:="main"),
        script(`type`:="text/javascript", src:="/js/ublogclient-jsdeps.js"),
        script(`type`:="text/javascript", src:="/js/ublogclient-fastopt.js"),
        script(`type`:="text/javascript", src:="/js/ublogclient-launcher.js")
      )
    )
}