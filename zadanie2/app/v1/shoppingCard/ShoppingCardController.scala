package v1.shoppingCard

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class ShoppingCardFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class ShoppingCardController @Inject()(cc: ShoppingCardControllerComponents)(
    implicit ec: ExecutionContext)
    extends ShoppingCardBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[ShoppingCardFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(ShoppingCardFormInput.apply)(ShoppingCardFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = ShoppingCardAction.async { implicit request =>
    logger.trace("index: ")
    shoppingCardResourceHandler.find.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def process: Action[AnyContent] = ShoppingCardAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: String): Action[AnyContent] = ShoppingCardAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      shoppingCardResourceHandler.lookup(id).map { post =>
        Ok(Json.toJson(post))
      }
  }

  private def processJsonPost[A]()(
      implicit request: ShoppingCardRequest[A]): Future[Result] = {
    def failure(badForm: Form[ShoppingCardFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: ShoppingCardFormInput) = {
      shoppingCardResourceHandler.create(input).map { post =>
        Created(Json.toJson(post)).withHeaders(LOCATION -> post.link)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
