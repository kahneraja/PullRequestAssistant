package controllers

import javax.inject._

import domain.User
import play.api.Configuration
import play.api.libs.json.JsValue
import play.api.mvc._
import repositories.MemberRepository

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  memberRepository: MemberRepository
) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index("PullRequestAssistant"))
  }

}
