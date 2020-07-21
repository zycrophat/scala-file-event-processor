package zycrophat.fileeventprocessor

import java.security.MessageDigest
import java.time.LocalDateTime

import com.azure.cosmos.{CosmosClient, CosmosClientBuilder}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, _}
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.{BindingName, BlobTrigger, FunctionName}

import scala.util.{Failure, Success, Try}


class MyFunction extends LazyLoggingTakingImplicitExecutionContext {

  {
    logger.info(s"MyFunction instantiated. $BuildInfo")
  }

  private implicit val fileMetadataCodec: JsonValueCodec[FileMetadata] = JsonCodecMaker.make
  private val mapper = new ObjectMapper
  private object CosmosDb {
    private val dbConn = System.getenv("DB_CONN_ENDPOINT")
    val dbClient: Option[CosmosClient] = Option(new CosmosClientBuilder()
      .endpoint(dbConn)
      .key(System.getenv("DB_CONN_KEY"))
      .buildClient())
  }

  @FunctionName("ScalaFunction")
  def run(@BlobTrigger(connection = "storageConn", dataType = "", name = "myblob", path = "blobs/{name}") myblob: String,
          @BindingName("name") fileName: String)(implicit context: ExecutionContext): Unit = {
    context.getLogger.info("Scala trigger processed a request.")
    executionLogger.info(s"CosmosDb: ${CosmosDb.dbClient.isDefined}")
    executionLogger.info(s"Foobar1339: $fileName")

    CosmosDb.dbClient.map { dbClient =>
      val container = dbClient.getDatabase("blobfunctionsdb").getContainer("blobfunctionsContainer1")

      Try {
        val id = MessageDigest.getInstance("SHA-256")
          .digest(fileName.getBytes("UTF-8"))
          .map("%02x".format(_)).mkString
        container.upsertItem(writeToJsonNode(FileMetadata(id, fileName, LocalDateTime.now())))
      }
    } match {
      case Some(upsertTry) => upsertTry match {
        case Failure(exception) => executionLogger.error(s"CosmosDB upsert failed", exception)
        case Success(r) => executionLogger.info(s"CosmosDB upsert complete. Status code: ${r.getStatusCode}")
      }
      case None => executionLogger.error(s"CosmosDB upsert could not be tried. dbClient may be uninitialized")
    }

  }

  private def writeToJsonNode[X](value: X)(implicit codec: JsonValueCodec[X]) =
    mapper.readTree(writeToString(value))

}


