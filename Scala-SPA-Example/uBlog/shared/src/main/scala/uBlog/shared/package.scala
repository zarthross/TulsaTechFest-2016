package uBlog

import upickle.Js

package object shared {
  implicit val javaInstantWriter = upickle.default.Writer[java.time.Instant]{
    case t => Js.Str(t.getEpochSecond.toString)
  }
  implicit val javaInstantReader = upickle.default.Reader[java.time.Instant]{
    case Js.Str(epoch) => java.time.Instant.ofEpochSecond(epoch.toLong)
  }
}