package v1.products

import play.api.MarkerContext
import play.api.libs.json._

import javax.inject.{Inject, Provider}
import scala.concurrent.{ExecutionContext, Future}

/**
  * DTO for displaying product information.
  */
case class ProductResource(id: String, link: String, title: String, price: String, category: String)

object ProductResource {
  /**
    * Mapping to read/write a ProductResource out as a JSON value.
    */
    implicit val format: Format[ProductResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[ProductResource]]
  */
class ProductsResourceHandler @Inject()(
                                     routerProvider: Provider[ProductsRouter],
                                     productsRepository: ProductsRepository)(implicit ec: ExecutionContext) {

  def create(productsInput: ProductsFormInput)(
      implicit mc: MarkerContext): Future[ProductResource] = {
    var index = productsRepository.size()

    val data = ProductData(ProductId((index + 1).toString), productsInput.title, productsInput.price, productsInput.category)

    productsRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(id: String)(
      implicit mc: MarkerContext): Future[Option[ProductResource]] = {
    val postFuture = productsRepository.get(ProductId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[ProductResource]] = {
    productsRepository.list().map { productDataList =>
      productDataList.map(productData => createPostResource(productData))
    }
  }

  def delete(id: String)(
    implicit mc: MarkerContext): Future[Option[ProductResource]] = {
    val postFuture = productsRepository.delete(ProductId(id))
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def update(id: String, productInput: ProductsFormInput)(
    implicit mc: MarkerContext): Future[ProductResource] = {
    val data = ProductData(ProductId(id), productInput.title, productInput.price, productInput.category)
    productsRepository.update(ProductId(id), data).map { id =>
      createPostResource(data)
    }
  }

  private def createPostResource(p: ProductData): ProductResource = {
    ProductResource(p.id.toString, routerProvider.get.link(p.id), p.title, p.price, p.category)
  }

}
