import com.google.inject.AbstractModule
import java.time.Clock

import gateways._
import repositories._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bind(classOf[ContributorRepository]).to(classOf[ContributorRepositoryImpl])
    bind(classOf[GitHubGateway]).to(classOf[GitHubGatewayImpl])
    bind(classOf[SlackGateway]).to(classOf[SlackGatewayImpl])
    bind(classOf[HttpClient]).to(classOf[HttpClientImpl])
    bind(classOf[GatewayConfig]).to(classOf[GatewayConfigImpl])
    bind(classOf[TimeProvider]).to(classOf[TimeProviderImpl])
    bind(classOf[MetricRepository]).to(classOf[MetricRepositoryImpl])
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl])
  }

}
