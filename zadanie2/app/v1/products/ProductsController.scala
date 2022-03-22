package v1.products

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class ProductsFormInput(title: String, price: String, category: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class ProductsController @Inject()(cc: ProductControllerComponents)(
    implicit ec: ExecutionContext)
    extends ProductBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[ProductsFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "price" -> text,
        "category" -> text
      )(ProductsFormInput.apply)(ProductsFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = ProductsAction.async { implicit request =>
    logger.trace("index: ")
    productsResourceHandler.find.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def process: Action[AnyContent] = ProductsAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: String): Action[AnyContent] = ProductsAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      productsResourceHandler.lookup(id).map { product =>
        Ok(Json.toJson(product))
      }
  }

  private def processJsonPost[A]()(
      implicit request: ProductRequest[A]): Future[Result] = {
    def failure(badForm: Form[ProductsFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: ProductsFormInput) = {
      productsResourceHandler.create(input).map { product =>
        Created(Json.toJson(product)).withHeaders(LOCATION -> product.link)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
