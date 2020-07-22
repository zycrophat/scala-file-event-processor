package zycrophat
import java.time.LocalDateTime


package object fileeventprocessor {
  case class FileMetadata (id: String, name: String, timestamp: LocalDateTime)
}
