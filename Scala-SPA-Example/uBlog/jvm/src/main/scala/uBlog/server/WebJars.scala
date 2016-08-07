package uBlog.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator

object WebJars {
  def apply(implicit as: ActorSystem): Route = {
    path(Segment / Remaining) { (jar,file) =>
      get {
        val loc = locate(jar,file)
        getFromResource(loc)
      }
    }
  }
  val webJarAssetLocator = new WebJarAssetLocator()

  private def locate(jar:String, file: String): String = {
    webJarAssetLocator.getFullPath(jar,file)
  }
}
