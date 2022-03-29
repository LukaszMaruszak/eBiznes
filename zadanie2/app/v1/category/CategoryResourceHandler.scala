package v1.category

import play.api.MarkerContext
import play.api.libs.json._

import javax.inject.{Inject, Provider}
import scala.concurrent.{ExecutionContext, Future}

/**
  * DTO for displaying category information.
  */
case class CategoryResource(id: String, link: String, name: String)

object CategoryResource {
  /**
    * Mapping to read/write a CategoryResource out as a JSON value.
    */
  implicit val format: Format[CategoryResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[CategoryResource]]
  */
class CategoryResourceHandler @Inject()(
                                         routerProvider: Provider[CategoryRouter],
                                         categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {

  def create(categoryInput: CategoryFormInput)(
    implicit mc: MarkerContext): Future[CategoryResource] = {
    val data = CategoryData(CategoryId("999"), categoryInput.name)
    // We don't actually create the post, so return what we have
    categoryRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(id: String)(
    implicit mc: MarkerContext): Future[Option[CategoryResource]] = {
    val postFuture = categoryRepository.get(CategoryId(id))
    postFuture.map { maybeCategoryData =>
      maybeCategoryData.map { categoryData =>
        createPostResource(categoryData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[CategoryResource]] = {
    categoryRepository.list().map { categoryDataList =>
      categoryDataList.map(categoryData => createPostResource(categoryData))
    }
  }

  private def createPostResource(p: CategoryData): CategoryResource = {
    CategoryResource(p.id.toString, routerProvider.get.link(p.id), p.name)
  }

}
