package v1.category

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class CategoryFormInput(name: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class CategoryController @Inject()(cc: CategoryControllerComponents)(
    implicit ec: ExecutionContext)
    extends CategoryBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[CategoryFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "name" -> text,
      )(CategoryFormInput.apply)(CategoryFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = CategoryAction.async { implicit request =>
    logger.trace("index: ")
    categoryResourceHandler.find.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def process: Action[AnyContent] = CategoryAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: String): Action[AnyContent] = CategoryAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      categoryResourceHandler.lookup(id).map { product =>
        Ok(Json.toJson(product))
      }
  }

  private def processJsonPost[A]()(
      implicit request: CategoryRequest[A]): Future[Result] = {
    def failure(badForm: Form[CategoryFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: CategoryFormInput) = {
      categoryResourceHandler.create(input).map { product =>
        Created(Json.toJson(product)).withHeaders(LOCATION -> product.name)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
