package controllers

import javax.inject._

import domain.Slack.AuthTokenRequest
import gateways.SlackGateway
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repositories.{MetricRepository, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class SlackController @Inject()(
  cc: ControllerComponents,
  metricRepository: MetricRepository,
  slackGateway: SlackGateway,
  userRepository: UserRepository
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def createToken: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[AuthTokenRequest].fold(
      _ => {
        Future.successful(BadRequest(Json.obj()))
      },
      request => {
        val updatedUser = for {
          response <- slackGateway.createAccessToken(request)
          user <- userRepository.find(request.userId)
          _ <- userRepository.updateSlackToken(user.get, response.access_token)
          updatedUser <- userRepository.find(request.userId)
        } yield updatedUser
        updatedUser.map { user =>
          Ok(Json.toJson(user.get))
        }
      }
    )
  }
}
