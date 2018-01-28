package domain.GitHub

import java.time.LocalDate

import play.api.libs.json.{Json, OFormat}

case class Metric(
  title: String,
  url: String,
  created: LocalDate,
  closed: LocalDate,
  hours: Int,
  changes: Int
) {
}

object Metric {
  implicit val format: OFormat[Metric] = Json.format[Metric]
}
