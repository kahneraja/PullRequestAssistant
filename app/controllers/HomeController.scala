package controllers

import javax.inject._

import domain.GitHub.AuthTokenRequest
import gateways.GitHubGateway
import play.api.Logger
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc._
import repositories.MetricsRepository
import useCases.CollectMetricsUseCase

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  metricsRepository: MetricsRepository,
  gitHubGateway: GitHubGateway
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index("PullRequestAssistant"))
  }

  def metrics: Action[AnyContent] = Action.async {
    metricsRepository.findAll().map { metrics =>
      Ok(Json.toJson(metrics))
    }
  }

  def reset: Action[AnyContent] = Action {
    Logger.info("Execute: CollectMetricsUseCase")
    new CollectMetricsUseCase(
      gitHubGateway = gitHubGateway,
      metricsRepository = metricsRepository
    ).execute()
    Ok(Json.obj())
  }

  def auth: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[AuthTokenRequest].fold(
      _ => {
        Future.successful(BadRequest(Json.obj()))
      },
      authTokenRequest => {
        gitHubGateway.createAccessToken(authTokenRequest).map { accessToken =>
          Ok(Json.toJson(accessToken))
        }
      }
    )
  }
}
