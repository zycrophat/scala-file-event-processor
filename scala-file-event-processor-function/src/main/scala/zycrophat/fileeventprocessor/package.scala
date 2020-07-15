package zycrophat
import java.time.{LocalDateTime}


package object fileeventprocessor {
  case class FileMetadata (name: String, timestamp: LocalDateTime)
}
