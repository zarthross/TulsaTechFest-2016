package uBlog.client

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, ReactElement}


object MasterTemplate {
  def apply[T](contents: ReactElement) =  {
    <.div(
      <.div(^.`class` := "blog-masthead") (
        <.div(^.className := "container") (
          <.nav(^.`class` := "blog-nav") (
            <.a(^.`class`:="blog-nav-item", ^.href:="#")("Home")
          )
        )
      ),
      <.div(^.`class` := "container") (
        <.div(^.className:="blog-header")(
          <.h1(^.`class`:="blog-title")("uBlog Example"),
          <.p(^.`class`:="lead blog-description")("uBlogged Blog Blog Blog?")
        ),
        <.div(^.`class`:="row") (
          <.div(^.`class`:="col-sm-8 blog-main") (
            contents
          ),
          SideBar()
        )
      ),
      <.footer(^.`class`:="blog-footer") (
        <.p(
          "Blog template built for ",
          <.a(^.href:="http://getbootstrap.com")("Bootstrap"),
          " by ",
          <.a(^.href:="http://twitter.com")("@mdo"),
          "."
        ),
        <.p(<.a(^.href:="#")("Back to top"))
      )
    )
  }
}
