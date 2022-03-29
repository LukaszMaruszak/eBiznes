import javax.inject._
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import v1.category._
import v1.shoppingCard._
import v1.products._

/**
  * Sets up custom components for Play.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind[ShoppingCardRepository].to[ShoppingCardRepositoryImpl].in[Singleton]()

    bind[ProductsRepository].to[ProductsRepositoryImpl].in[Singleton]()

    bind[CategoryRepository].to[CategoryRepositoryImpl].in[Singleton]()
  }
}
