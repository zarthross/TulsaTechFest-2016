package uBlog.shared

trait BlogApi {
  def list(): Set[(String, java.time.Instant)]
  def create(title: String, body: String, tags: Seq[String] = Seq.empty): BlogPost
  def createOrUpdate(oldTitle: Option[String], newTitle: String, tags: Seq[String], body: String): BlogPost
  def delete(title: String): Boolean
  def info(title: String): Option[BlogPost]
  def tags(): Set[String]
}

case class BlogPost(title: String, tags: Seq[String],
                    body: String,
                    createdOn: java.time.Instant,
                    updatedOn: java.time.Instant)