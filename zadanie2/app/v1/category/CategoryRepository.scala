package v1.category

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.collection.mutable.ListBuffer

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
  def size()(implicit mc: MarkerContext): Int

  def create(data: CategoryData)(implicit mc: MarkerContext): Future[CategoryId]

  def list()(implicit mc: MarkerContext): Future[Iterable[CategoryData]]

  def get(id: CategoryId)(implicit mc: MarkerContext): Future[Option[CategoryData]]

  def delete(id: CategoryId)(implicit mc: MarkerContext): Future[Option[CategoryData]]

  def update(id: CategoryId, data: CategoryData)(implicit mc: MarkerContext): Future[Iterable[CategoryData]]
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

  private val categoryList = ListBuffer(
    CategoryData(CategoryId("1"), "Fruits"),
    CategoryData(CategoryId("2"), "Vegetables"),
    CategoryData(CategoryId("3"), "Dairy"),
  )

  override def size()(
    implicit mc: MarkerContext): Int = {
    categoryList.length
  }

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
      categoryList.find(category => category.id == id)
    }
  }

  override def delete(id: CategoryId)(
    implicit mc: MarkerContext): Future[Option[CategoryData]] = {
    Future {
      logger.trace(s"delete: id = $id")
      var elementIndex = categoryList.indexWhere(category => category.id == id).toInt
      var removed: CategoryData = null
      if(elementIndex != -1) {
        removed = categoryList.remove(elementIndex)
      }

      Option(removed)
    }
  }

  override def create(data: CategoryData)(implicit mc: MarkerContext): Future[CategoryId] = {
    Future {
      logger.trace(s"create category: data = $data")
      categoryList += data
      data.id
    }
  }

  override def update(id: CategoryId, data: CategoryData)( implicit mc: MarkerContext): Future[Iterable[CategoryData]] = {
    logger.trace(s"id: id = $id")
    logger.trace(s"update: data = $data")

    Future {
      var elementIndex = categoryList.indexWhere(category => category.id == id).toInt
      if(elementIndex != -1) {
        categoryList.update(elementIndex, data)
      }
      else {
        categoryList += data
      }
      categoryList
    }
  }
}
