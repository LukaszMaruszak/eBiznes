package v1.shoppingCard

import javax.inject.{Inject, Singleton}
import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

final case class ShoppingCardData(id: ShoppingCardId,  title: String, quantity: String, price: String)

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
  def size()(implicit mc: MarkerContext): Int

  def create(data: ShoppingCardData)(implicit mc: MarkerContext): Future[ShoppingCardId]

  def list()(implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]]

  def get(id: ShoppingCardId)(implicit mc: MarkerContext): Future[Option[ShoppingCardData]]

  def delete(id: ShoppingCardId)(implicit mc: MarkerContext): Future[Option[ShoppingCardData]]

  def update(id: ShoppingCardId, data: ShoppingCardData)(implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]]
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

  private val shoppingCardList = ListBuffer(
    ShoppingCardData(ShoppingCardId("1"), "Banana", "2", "5zł"),
    ShoppingCardData(ShoppingCardId("2"), "Orange", "10kg", "6zł"),
    ShoppingCardData(ShoppingCardId("3"), "Potato", "5kg", "7zł"),
    ShoppingCardData(ShoppingCardId("4"), "Cucumber", "1kg", "2.5zł"),
  )

  override def size()(
    implicit mc: MarkerContext): Int = {
    shoppingCardList.length
  }

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]] = {
    Future {
      logger.trace(s"list: ")
      shoppingCardList
    }
  }

  override def get(id: ShoppingCardId)(
      implicit mc: MarkerContext): Future[Option[ShoppingCardData]] = {
    Future {
      logger.trace(s"get: id = $id")
      shoppingCardList.find(shoppingCard => shoppingCard.id == id)
    }
  }

  override def delete(id: ShoppingCardId)(
    implicit mc: MarkerContext): Future[Option[ShoppingCardData]] = {
    Future {
      logger.trace(s"delete: id = $id")
      var elementIndex = shoppingCardList.indexWhere(category => category.id == id).toInt
      var removed: ShoppingCardData = null
      if(elementIndex != -1) {
        removed = shoppingCardList.remove(elementIndex)
      }

      Option(removed)
    }
  }

  def create(data: ShoppingCardData)(implicit mc: MarkerContext): Future[ShoppingCardId] = {
    Future {
      logger.trace(s"create: data = $data")
      shoppingCardList += data
      data.id
    }
  }

  override def update(id: ShoppingCardId, data: ShoppingCardData)( implicit mc: MarkerContext): Future[Iterable[ShoppingCardData]] = {
    logger.trace(s"id: id = $id")
    logger.trace(s"update: data = $data")

    Future {
      var elementIndex = shoppingCardList.indexWhere(product => product.id == id).toInt
      if(elementIndex != -1) {
        shoppingCardList.update(elementIndex, data)
      }
      else {
        shoppingCardList += data
      }
      shoppingCardList
    }
  }
}
