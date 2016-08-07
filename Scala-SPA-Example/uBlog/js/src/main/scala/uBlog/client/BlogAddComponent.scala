package uBlog.client

import scala.concurrent.Future

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import uBlog.shared._
import autowire._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import japgolly.scalajs.react.CallbackTo.MapGuard

object BlogAddComponent {
  type Title = Option[String]
  case class SimplePost(title: String, body: String, tags: Seq[String])
  lazy val emptyBlog = SimplePost("", "", Seq.empty[String])

  def apply(title: Title) =
    ReactComponentB[Title]("BlogInfo")
      .initialState(Option.empty[SimplePost])
      .renderBackend[Backend]
      .componentDidMount(_.backend.didMount)
      .build(title)

  class Backend($: BackendScope[Title, Option[SimplePost]]) {
    def didMount: Callback = $.props.flatMap { (title: Title) =>
      val r: Future[Callback] = title.map({ t =>
        AjaxClient[BlogApi].info(t).call().map { post =>
          post.map(x => SimplePost(x.title, x.body, x.tags))
              .getOrElse(emptyBlog)
        }
      }).getOrElse(Future.successful(emptyBlog))
        .map { t =>
          $.setState(Option(t))
        }
      Callback.future(r)
    }

    def onChange(t: (SimplePost, String) => SimplePost)(e: ReactEventI) = {
      val newValue = e.target.value
      $.modState(_.map(t(_, newValue)))
    }


    def handleSubmit(e: ReactEventI): Callback =
      e.preventDefaultCB >> $.props.flatMap { (p: Title) =>
        $.state.flatMap { (so: Option[SimplePost]) =>
          val s = so.get
          val r = AjaxClient[BlogApi].createOrUpdate(p,s.title, s.tags, s.body).call().map { b =>
            SimplePost(b.title, b.body, b.tags)
          } map { sp => $.setState(Some(sp)) }
          Callback.future(r)
        }
      }

    def render(state: Option[SimplePost]): ReactElement = {
      val SimplePost(postTitle, postBody, postTags) = state.getOrElse(SimplePost("", "", Seq.empty[String]))
      MasterTemplate(
        <.form(^.onSubmit ==> handleSubmit)(
          <.label("Title"),
          <.input.text(^.value:=postTitle, ^.onChange ==> onChange((b,v) => b.copy(title = v))),
          <.br(),
          <.label("Tags"),
          <.input.text(^.value:=postTags.mkString(","), ^.onChange ==> onChange((b,v) => b.copy(tags = v.split(',')))),
          <.br(),
          <.label("Text"),
          <.input.text(^.value:=postBody, ^.onChange ==> onChange((b,v) => b.copy(body = v))),
          <.button("Submit")
        )
      )
    }
  }
}
