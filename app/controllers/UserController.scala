package controllers

import javax.inject._

import domain.GitHub.Org
import gateways.SlackGateway
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repositories.{OrgRepository, UserRepository}
import requests.AddOrgRequest

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class UserController @Inject()(
  cc: ControllerComponents,
  orgRepository: OrgRepository,
  userRepository: UserRepository,
  slackGatway: SlackGateway
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def addOrg(userId: String): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[AddOrgRequest].fold(
      _ => {
        Future.successful(BadRequest(Json.obj()))
      },
      request => {
        val newOrg = new Org(request.id, request.url, userId)
        orgRepository.find(request.id).flatMap {
          case None => orgRepository.insert(new Org(request.id, request.url, userId)).map { _ =>
            Ok(Json.toJson(newOrg))
          }
          case _ => Future.successful(BadRequest(Json.obj()))
        }
      }
    )
  }

  def getOrgs(userId: String): Action[AnyContent] = Action.async {
    orgRepository.findByUserId(userId).map { orgs =>
      Ok(Json.toJson(orgs))
    }
  }

  def getSlackMembers(userId: String): Action[AnyContent] = Action.async {
    userRepository.find(userId).flatMap {
      case Some(user) =>
        slackGatway.getMembers(user.slackToken).map { members =>
          Ok(Json.toJson(members))
        }
      case _ => Future.successful(BadRequest(Json.obj()))
    }
  }
}
