package uBlog.server

import uBlog.shared._

object BlogApiImpl extends BlogApi {
  private def cleanTitle(t: String) = t.toLowerCase.trim
  private var blogPosts = Map.empty[String, BlogPost]

  def list(): Set[(String, java.time.Instant)] = blogPosts.values.map(x => x.title -> x.updatedOn).toSet

  def create(title: String, body: String, tags: Seq[String] = Seq.empty): BlogPost = {
    val bp = BlogPost(title, tags, body, java.time.Instant.now(), java.time.Instant.now())
    blogPosts = blogPosts + (cleanTitle(title) -> bp)
    bp
  }

  def update(oldTitle: String, newTitle: String, tags: Seq[String], body: String): BlogPost = {
    val cleaned = cleanTitle(oldTitle)
    blogPosts.get(cleaned) match {
      case Some(b) =>
        val nb = b.copy(title = newTitle, body = body, tags = tags, updatedOn = java.time.Instant.now)
        blogPosts = blogPosts - cleaned + (cleanTitle(newTitle) -> nb)
        nb
      case None => create(newTitle, body, tags)
    }
  }

  def createOrUpdate(oldTitle: Option[String], newTitle: String, tags: Seq[String], body: String): BlogPost = {
    oldTitle.map(ot => update(ot, newTitle, tags, body)).getOrElse(create(newTitle,body, tags))
  }

  def delete(title: String): Boolean = {
    val cleaned = cleanTitle(title)
    blogPosts.get(cleaned) match {
      case Some(t) =>
        blogPosts = blogPosts - cleaned
        true
      case None =>
        false
    }
  }
  def info(title: String): Option[BlogPost] = blogPosts.get(cleanTitle(title))

  def tags(): Set[String]  = blogPosts.values.flatMap(_.tags).toSet


  create("test", "Test Blog POOOST")
  create("test2", "Another Test Blog POOOST")
  create("test3", "Third Test Blog POOOST")
}

