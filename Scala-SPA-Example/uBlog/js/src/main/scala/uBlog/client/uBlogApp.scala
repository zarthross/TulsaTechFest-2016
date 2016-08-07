package uBlog.client

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import autowire._
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalacss.Defaults._
import scalacss.ScalaCssReact._

import japgolly.scalajs.react.ReactDOM
import japgolly.scalajs.react.extra.router.{BaseUrl, Router}
import org.scalajs.dom

@JSExport
object uBlogApp extends js.JSApp {
  lazy val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))
  lazy val (router, routerCtl) = uBlogRouter.componentAndCtl(baseUrl)

  @JSExport
  def main(): Unit = {
    val elem = dom.document.getElementById("main")

    router() render elem
  }
}
