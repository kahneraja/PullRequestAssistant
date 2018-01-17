package gateways

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

object Extensions {
  implicit class RichLocalDateTime(localDateTime: LocalDateTime) {
    def isDuringOfficeHours: Boolean = {
      val dayName = localDateTime.getDayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
      dayName match {
        case "Saturday" => false
        case "Sunday" => false
        case _ =>
          localDateTime.getHour match {
            case h if h < 9 => false
            case h if h >= 18 => false
            case _ => true
          }
      }
    }

    val minutesToNextHour = Math.abs(localDateTime.getMinute() - 60) + 0
  }
}
