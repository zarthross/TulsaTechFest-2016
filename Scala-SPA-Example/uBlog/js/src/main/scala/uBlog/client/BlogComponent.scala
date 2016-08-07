package uBlog.client

import japgolly.scalajs.react.{ReactComponentB, ReactComponentU, ReactElement, TopNode}
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._

object BlogComponent {
  def apply(title: Option[String]): ReactComponentU[Option[String], Unit, Unit, TopNode] =
    ReactComponentB[Option[String]]("Blog")
      .render( $ =>
        MasterTemplate(
          $.props match {
            case Some(t) => BlogInfo(t)
            case None => <.p("Select on Blog on the right")
          }
        )
      )
      .build(title)
}
