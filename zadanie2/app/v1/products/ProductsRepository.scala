package v1.products

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.collection.mutable.ListBuffer

final case class ProductData(id: ProductId, title: String, price: String, category: String)

class ProductId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object ProductId {
  def apply(raw: String): ProductId = {
    require(raw != null)
    new ProductId(Integer.parseInt(raw))
  }
}

class ProductsExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the ProductsRepository.
  */
trait ProductsRepository {
  def size()(implicit mc: MarkerContext): Int

  def create(data: ProductData)(implicit mc: MarkerContext): Future[ProductId]

  def list()(implicit mc: MarkerContext): Future[Iterable[ProductData]]

  def get(id: ProductId)(implicit mc: MarkerContext): Future[Option[ProductData]]

  def delete(id: ProductId)(implicit mc: MarkerContext): Future[Option[ProductData]]

  def update(id: ProductId, data: ProductData)(implicit mc: MarkerContext): Future[Iterable[ProductData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class ProductsRepositoryImpl @Inject()()(implicit ec: ProductsExecutionContext)
    extends ProductsRepository {

  private val logger = Logger(this.getClass)

  private val productsList = ListBuffer(
    ProductData(ProductId("1"), "Banana", "5 zł", "Fruit"),
    ProductData(ProductId("2"), "Apple", "4 zł", "Fruit"),
    ProductData(ProductId("3"), "Orange", "10 zł", "Fruit"),
    ProductData(ProductId("4"), "Carrot", "2 zł", "Vegetable"),
    ProductData(ProductId("5"), "Tomato", "6 zł", "Vegetable")
  )

  override def size()(
    implicit mc: MarkerContext): Int = {
    productsList.length
  }

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[ProductData]] = {
    Future {
      logger.trace(s"list: ")
      productsList
    }
  }

  override def get(id: ProductId)(
      implicit mc: MarkerContext): Future[Option[ProductData]] = {
    Future {
      logger.trace(s"get: id = $id")
      productsList.find(product => product.id == id)
    }
  }

  override def delete(id: ProductId)(
    implicit mc: MarkerContext): Future[Option[ProductData]] = {
    Future {
      logger.trace(s"delete: id = $id")
      var elementIndex = productsList.indexWhere(category => category.id == id).toInt
      var removed: ProductData = null
      if(elementIndex != -1) {
        removed = productsList.remove(elementIndex)
      }

      Option(removed)
    }
  }

  def create(data: ProductData)(implicit mc: MarkerContext): Future[ProductId] = {
    Future {
      logger.trace(s"create: data = $data")
      productsList += data
      data.id
    }
  }

  override def update(id: ProductId, data: ProductData)( implicit mc: MarkerContext): Future[Iterable[ProductData]] = {
    logger.trace(s"id: id = $id")
    logger.trace(s"update: data = $data")

    Future {
      var elementIndex = productsList.indexWhere(product => product.id == id).toInt
      if(elementIndex != -1) {
        productsList.update(elementIndex, data)
      }
      else {
        productsList += data
      }
      productsList
    }
  }
}
