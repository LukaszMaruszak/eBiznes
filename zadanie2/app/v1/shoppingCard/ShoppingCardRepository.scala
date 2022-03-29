package v1.shoppingCard

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

final case class ShoppingCardData(id: ShoppingCardId, title: String, body: String)

class ShoppingCardId private(val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object ShoppingCardId {
  def apply(raw: String): ShoppingCardId = {
    require(raw != null)
    new ShoppingCardId(Integer.parseInt(raw))
  }
}

class ShoppingCardExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait ShoppingCardRepository {
  def create(data: ShoppingCardData)(implicit mc: MarkerContext): Future[ShoppingCardId]

  def list()(implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]]

  def get(id: ShoppingCardId)(implicit mc: MarkerContext): Future[Option[ShoppingCardData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class ShoppingCardRepositoryImpl @Inject()()(implicit ec: ShoppingCardExecutionContext)
    extends ShoppingCardRepository {

  private val logger = Logger(this.getClass)

  private val postList = List(
    ShoppingCardData(ShoppingCardId("1"), "title 1", "blog post 1"),
    ShoppingCardData(ShoppingCardId("2"), "title 2", "blog post 2"),
    ShoppingCardData(ShoppingCardId("3"), "title 3", "blog post 3"),
    ShoppingCardData(ShoppingCardId("4"), "title 4", "blog post 4"),
    ShoppingCardData(ShoppingCardId("5"), "title 5", "blog post 5")
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]] = {
    Future {
      logger.trace(s"list: ")
      postList
    }
  }

  override def get(id: ShoppingCardId)(
      implicit mc: MarkerContext): Future[Option[ShoppingCardData]] = {
    Future {
      logger.trace(s"get: id = $id")
      postList.find(post => post.id == id)
    }
  }

  def create(data: ShoppingCardData)(implicit mc: MarkerContext): Future[ShoppingCardId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
