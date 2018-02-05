package controllers

import java.util.UUID
import javax.inject._

import domain.GitHub.AuthTokenRequest
import domain.User
import gateways.GitHubGateway
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repositories.{MetricRepository, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class GitHubController @Inject()(
  cc: ControllerComponents,
  metricRepository: MetricRepository,
  gitHubGateway: GitHubGateway,
  userRepository: UserRepository
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {


  def createToken: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[AuthTokenRequest].fold(
      _ => {
        Future.successful(BadRequest(Json.obj()))
      },
      authTokenRequest => {
        gitHubGateway.createAccessToken(authTokenRequest).map { response =>
          val user = new User(UUID.randomUUID().toString, response.access_token, "")
          userRepository.insert(user)
          Ok(Json.toJson(user))
        }
      }
    )
  }

}
