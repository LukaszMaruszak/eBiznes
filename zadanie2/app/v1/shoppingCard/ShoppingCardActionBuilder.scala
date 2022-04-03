package v1.shoppingCard

import javax.inject.Inject

import net.logstash.logback.marker.LogstashMarker
import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * A wrapped request for Shopping Card resources.
  *
  * This is commonly used to hold request-specific information like
  * security credentials, and useful shortcut methods.
  */
trait ShoppingCardRequestHeader
    extends MessagesRequestHeader
    with PreferredMessagesProvider
class ShoppingCardRequest[A](request: Request[A], val messagesApi: MessagesApi)
    extends WrappedRequest(request)
    with ShoppingCardRequestHeader

/**
  * Provides an implicit marker that will show the request in all logger statements.
  */
trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(
      implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker(
        "remoteAddress" -> request.remoteAddress)
    }
  }

}

/**
  * The action builder for the Shopping Card resource.
  *
  * This is the place to put logging, metrics, to augment
  * the request with contextual data, and manipulate the
  * result.
  */
class ShoppingCardActionBuilder @Inject()(messagesApi: MessagesApi,
                                          playBodyParsers: PlayBodyParsers)(
    implicit val executionContext: ExecutionContext)
    extends ActionBuilder[ShoppingCardRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type ShoppingCardRequestBlock[A] = ShoppingCardRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: ShoppingCardRequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(
      request)
    logger.trace(s"invokeBlock: ")

    val future = block(new ShoppingCardRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

/**
  * Packages up the component dependencies for the Shopping Card controller.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class ShoppingCardControllerComponents @Inject()(
                                                       shoppingCardActionBuilder: ShoppingCardActionBuilder,
                                                       shoppingCardResourceHandler: ShoppingCardResourceHandler,
                                                       actionBuilder: DefaultActionBuilder,
                                                       parsers: PlayBodyParsers,
                                                       messagesApi: MessagesApi,
                                                       langs: Langs,
                                                       fileMimeTypes: FileMimeTypes,
                                                       executionContext: scala.concurrent.ExecutionContext)
    extends ControllerComponents

/**
  * Exposes actions and handler to the ShoppingCardController by wiring the injected state into the base class.
  */
class ShoppingCardBaseController @Inject()(pcc: ShoppingCardControllerComponents)
    extends BaseController
    with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = pcc

  def ShoppingCardAction: ShoppingCardActionBuilder = pcc.shoppingCardActionBuilder

  def shoppingCardResourceHandler: ShoppingCardResourceHandler = pcc.shoppingCardResourceHandler
}
