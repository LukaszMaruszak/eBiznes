package v1.shoppingCard

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying post information.
  */
case class ShoppingCardResource(id: String, title: String, quantity: String, price: String)

object ShoppingCardResource {
  /**
    * Mapping to read/write a PostResource out as a JSON value.
    */
    implicit val format: Format[ShoppingCardResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[ShoppingCardResource]]
  */
class ShoppingCardResourceHandler @Inject()(
                                             routerProvider: Provider[ShoppingCardRouter],
                                             shoppingCardRepository: ShoppingCardRepository)(implicit ec: ExecutionContext) {

  def create(shoppingCardInput: ShoppingCardFormInput)(
      implicit mc: MarkerContext): Future[ShoppingCardResource] = {
    var index = shoppingCardRepository.size()

    val data = ShoppingCardData(ShoppingCardId((index + 1).toString), shoppingCardInput.title, shoppingCardInput.quantity, shoppingCardInput.price)
    shoppingCardRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(id: String)(
      implicit mc: MarkerContext): Future[Option[ShoppingCardResource]] = {
    val postFuture = shoppingCardRepository.get(ShoppingCardId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def delete(id: String)(
    implicit mc: MarkerContext): Future[Option[ShoppingCardResource]] = {
    val postFuture = shoppingCardRepository.delete(ShoppingCardId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[ShoppingCardResource]] = {
    shoppingCardRepository.list().map { postDataList =>
      postDataList.map(postData => createPostResource(postData))
    }
  }

  def update(id: String, shoppingCardFormInput: ShoppingCardFormInput)(
    implicit mc: MarkerContext): Future[ShoppingCardResource] = {
    val data = ShoppingCardData(ShoppingCardId(id), shoppingCardFormInput.title, shoppingCardFormInput.quantity, shoppingCardFormInput.price)
    shoppingCardRepository.update(ShoppingCardId(id), data).map { id =>
      createPostResource(data)
    }
  }

  private def createPostResource(p: ShoppingCardData): ShoppingCardResource = {
    ShoppingCardResource(p.id.toString, p.title, p.quantity, p.price)
  }

}
