package controllers

import java.util.UUID
import javax.inject._

import domain.GitHub.AuthTokenRequest
import domain.User
import gateways.{GitHubGateway, SlackGateway}
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repositories.{MetricRepository, UserRepository}
import useCases.CollectMetricsUseCase

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  metricRepository: MetricRepository,
  gitHubGateway: GitHubGateway,
  slackGateway: SlackGateway,
  userRepository: UserRepository
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index("PullRequestAssistant"))
  }

  def metrics: Action[AnyContent] = Action.async {
    metricRepository.findAll().map { metrics =>
      Ok(Json.toJson(metrics))
    }
  }

  def reset: Action[AnyContent] = Action {
    Logger.info("Execute: CollectMetricsUseCase")
    new CollectMetricsUseCase(
      gitHubGateway = gitHubGateway,
      metricRepository = metricRepository
    ).execute()
    Ok(Json.obj())
  }
}
