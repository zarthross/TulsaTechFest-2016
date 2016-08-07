package uBlog.client

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactComponentU, ReactElement, TopNode}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import uBlog.shared._
import autowire._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object BlogInfo {
  type Title = String
  def apply(title: Title) =
    ReactComponentB[Title]("BlogInfo")
      .initialState(Option.empty[BlogPost])
      .renderBackend[Backend]
      .componentDidMount(_.backend.didMount)
      .build(title)

  class Backend($: BackendScope[Title, Option[BlogPost]]) {
    def didMount = Callback {
      val t: Title = $.props.runNow()
      AjaxClient[BlogApi].info(t).call().map { post: Option[BlogPost] =>
        $.setState(post).runNow()
      }
    }

    def render(state: Option[BlogPost]): ReactElement =
      state match {
        case None => <.div("Could not find reqeuest blog post")
        case Some(s) =>
        <.div(^.`class` := "blog-post")(
          <.h2(^.`class` := "blog-post-title")(
            s.title
          ),
          <.p(^.`class` := "blog-post-meta")(
            s.updatedOn.toString
          ),
          <.div(
            <.ul(
              s.tags.map { t =>
                <.li(t)
              }
            )
          ),
          <.span(
            s.body
          )
        )
      }
  }
}
