package uBlog.client

import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, Router, RouterConfigDsl}

sealed trait MyPages
case object HomePage                      extends MyPages
case class BlogPage(title: String)        extends MyPages
case class UpdateBlog(title: String)      extends MyPages
case object NewBlog                       extends MyPages

object uBlogRouter {
  def componentAndCtl(baseUrl: BaseUrl) = Router.componentAndCtl(baseUrl, routerConfig.logToConsole)

  val routerConfig = RouterConfigDsl[MyPages].buildConfig { dsl =>
    import dsl._

     ( emptyRule
     | staticRoute(root, HomePage) ~> render(BlogComponent(None))
     | staticRoute("#", HomePage) ~> render(BlogComponent(None))
     | staticRoute("#blog-edit", NewBlog) ~> render(BlogAddComponent(None))
     | dynamicRouteCT("#blog" / string("[a-z0-9]+").caseClass[BlogPage]) ~> dynRender(x => BlogComponent(Option(x.title)))
     | dynamicRouteCT("#blog-edit" / string("[a-z0-9]+").caseClass[UpdateBlog]) ~> dynRender(x => BlogAddComponent(Option(x.title)))
     ).notFound(redirectToPage(HomePage)(Redirect.Replace))
  }
}
