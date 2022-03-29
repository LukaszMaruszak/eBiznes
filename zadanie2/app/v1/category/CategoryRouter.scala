package v1.category

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

/**
  * Routes and URLs to the CategoriesRouter controller.
  */
class CategoryRouter @Inject()(controller: CategoryController) extends SimpleRouter {
  val prefix = "/v1/category"

  def link(id: CategoryId): String = {
    import io.lemonlabs.uri.dsl._
    val url = prefix / id.toString
    url.toString()
  }

  override def routes: Routes = {
    case GET(p"/") =>
      controller.index

    case POST(p"/") =>
      controller.process

    case GET(p"/$id") =>
      controller.show(id)
  }

}
