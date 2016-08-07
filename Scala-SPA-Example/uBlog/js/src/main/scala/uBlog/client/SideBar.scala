package uBlog.client

import java.time.Instant

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import scala.scalajs.js
import js.JSConverters._
import autowire._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactComponentU, ReactElement, TopNode}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import uBlog.shared._

object SideBar {
  def apply() =
    ReactComponentB[Unit]("Sidebar")
      .initialState(Set.empty[(String, Instant)])
      .renderBackend[Backend]
      .componentDidMount(_.backend.didMount)
      .build()

  private type State = Set[(String, Instant)]
  class Backend($: BackendScope[Unit, State]) {
    def didMount = Callback {
      AjaxClient[BlogApi].list().call().map { data =>
        $.setState(data).runNow()
      }
    }

    def render(state: State): ReactElement =
      <.div(^.`class`:="col-sm-3 col-sm-offset-1 blog-sidebar") (
        <.div(^.`class`:="sidebar-module")(
          <.h4("Posts"),
          <.ol(^.`class`:="list-unstyled")(
            state.map { case (title,time) =>
              <.li(^.key:=title)(
                uBlogApp.routerCtl.link(BlogPage(title))(
                  s"$title on ${time.toString}"
                )
              )
            }
          )
        )
      )
  }
}
