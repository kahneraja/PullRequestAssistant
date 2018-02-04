package requests

import play.api.libs.json.{Json, OFormat}

case class AddOrgRequest(id: Int, url: String)

object AddOrgRequest {
  implicit val format: OFormat[AddOrgRequest] = Json.format[AddOrgRequest]
}
