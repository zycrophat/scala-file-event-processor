package zycrophat.fileeventprocessor

import java.time.LocalDateTime

import com.azure.cosmos.models.CosmosItemResponse
import com.azure.cosmos.{CosmosAsyncClient, CosmosClient, CosmosClientBuilder}
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.{BindingName, BlobTrigger, FunctionName}
import com.typesafe.scalalogging.LazyLogging
import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._

import scala.util.{Failure, Success, Try}


class MyFunction extends LazyLogging {

  {
    logger.info(s"MyFunction instantiated. $BuildInfo")
  }

  private implicit val fileMetadataCodec: JsonValueCodec[FileMetadata] = JsonCodecMaker.make

  private object CosmosDb {
    private val dbConn = System.getenv("DB_CONN_ENDPOINT")
    val dbClient: CosmosClient = new CosmosClientBuilder()
      .endpoint(dbConn)
      .key(System.getenv("DB_CONN_KEY"))
      .buildClient()
  }

  @FunctionName("ScalaFunction")
  def run(@BlobTrigger(connection = "storageConn", dataType = "", name = "myblob", path = "blobs/{name}") myblob: String,
          @BindingName("name") fileName: String,
           context: ExecutionContext): Unit = {


    context.getLogger.info("Scala trigger processed a request.")
    logger.info(s"CosmosDb: ${CosmosDb.dbClient == null}")
    logger.info(s"Foobar1338: $fileName")

    val container = CosmosDb.dbClient.getDatabase("blobfunctionsdb").getContainer("blobfunctionsContainer1")

    try {
      import com.fasterxml.jackson.databind.ObjectMapper
      val mapper = new ObjectMapper
      val r = container.upsertItem(mapper.readTree(writeToString(FileMetadata(name = fileName, timestamp = LocalDateTime.now()))))
      logger.info(s"CosmosDB update succeeded. Status code: ${r.getStatusCode}")
    } catch {
      case t: Throwable => logger.error(s"CosmosDB upsert failed", t)
    }

  }
}
