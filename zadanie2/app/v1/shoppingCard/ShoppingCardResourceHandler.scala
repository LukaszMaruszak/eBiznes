package v1.shoppingCard

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying post information.
  */
case class ShoppingCardResource(id: String, link: String, title: String, body: String)

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
    postRepository: ShoppingCardRepository)(implicit ec: ExecutionContext) {

  def create(postInput: ShoppingCardFormInput)(
      implicit mc: MarkerContext): Future[ShoppingCardResource] = {
    val data = ShoppingCardData(ShoppingCardId("999"), postInput.title, postInput.body)
    // We don't actually create the post, so return what we have
    postRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(id: String)(
      implicit mc: MarkerContext): Future[Option[ShoppingCardResource]] = {
    val postFuture = postRepository.get(ShoppingCardId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[ShoppingCardResource]] = {
    postRepository.list().map { postDataList =>
      postDataList.map(postData => createPostResource(postData))
    }
  }

  private def createPostResource(p: ShoppingCardData): ShoppingCardResource = {
    ShoppingCardResource(p.id.toString, routerProvider.get.link(p.id), p.title, p.body)
  }

}
