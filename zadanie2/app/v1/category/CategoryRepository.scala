package v1.category

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

final case class CategoryData(id: CategoryId, name: String)

class CategoryId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object CategoryId {
  def apply(raw: String): CategoryId = {
    require(raw != null)
    new CategoryId(Integer.parseInt(raw))
  }
}

class CategoryExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the CategoryRepository.
  */
trait CategoryRepository {
  def create(data: CategoryData)(implicit mc: MarkerContext): Future[CategoryId]

  def list()(implicit mc: MarkerContext): Future[Iterable[CategoryData]]

  def get(id: CategoryId)(implicit mc: MarkerContext): Future[Option[CategoryData]]
}

/**
  * A trivial implementation for the Category Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class CategoryRepositoryImpl @Inject()()(implicit ec: CategoryExecutionContext)
    extends CategoryRepository {

  private val logger = Logger(this.getClass)

  private val categoryList = List(
    CategoryData(CategoryId("1"), "Fruits"),
    CategoryData(CategoryId("2"), "Vegetables"),
    CategoryData(CategoryId("3"), "Dairy"),
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[CategoryData]] = {
    Future {
      logger.trace(s"list: ")
      categoryList
    }
  }

  override def get(id: CategoryId)(
      implicit mc: MarkerContext): Future[Option[CategoryData]] = {
    Future {
      logger.trace(s"get: id = $id")
      categoryList.find(post => post.id == id)
    }
  }

  def create(data: CategoryData)(implicit mc: MarkerContext): Future[CategoryId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
