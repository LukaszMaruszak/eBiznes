package v1.shoppingCard

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class ShoppingCardFormInput(title: String, quantity: String, price: String)

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
        "quantity" -> text,
        "price" -> text,

      )(ShoppingCardFormInput.apply)(ShoppingCardFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = shoppingCardAction.async { implicit request =>
    logger.trace("index: ")
    shoppingCardResourceHandler.find.map { shoppingCard =>
      Ok(Json.toJson(shoppingCard))
    }
  }

  def process: Action[AnyContent] = shoppingCardAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: String): Action[AnyContent] = shoppingCardAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      shoppingCardResourceHandler.lookup(id).map { shoppingCard =>
        Ok(Json.toJson(shoppingCard))
      }
  }

  def delete(id: String): Action[AnyContent] = shoppingCardAction.async {
    implicit request =>
      logger.trace(s"delete: id = $id")
      shoppingCardResourceHandler.delete(id).map { card =>
        Ok(Json.toJson(card))
      }
  }

  def update(id: String): Action[AnyContent] = shoppingCardAction.async {
    implicit request =>
      logger.trace(s"update: id = $id")
      processJsonUpdate(id)
  }


  private def processJsonPost[A]()(
      implicit request: ShoppingCardRequest[A]): Future[Result] = {
    def failure(badForm: Form[ShoppingCardFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: ShoppingCardFormInput) = {
      shoppingCardResourceHandler.create(input).map { shoppingCard =>
        Created(Json.toJson(shoppingCard))
      }
    }

    form.bindFromRequest().fold(failure, success)
  }

  private def processJsonUpdate[A](id: String)(
    implicit request: ShoppingCardRequest[A]): Future[Result] = {

    def failure(badForm: Form[ShoppingCardFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: ShoppingCardFormInput) = {
      shoppingCardResourceHandler.update(id, input).map { category =>
        Created(Json.toJson(category))
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
