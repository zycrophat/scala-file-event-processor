package zycrophat.fileeventprocessor

import java.time.LocalDateTime

import com.azure.cosmos.{CosmosAsyncClient, CosmosClientBuilder}
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.{BindingName, BlobTrigger, FunctionName}
import com.typesafe.scalalogging.LazyLogging
import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._

import scala.util.Try


class MyFunction extends LazyLogging {

  {
    logger.info(s"MyFunction instantiated. $BuildInfo")
  }

  private implicit val fileMetadataCodec: JsonValueCodec[FileMetadata] = JsonCodecMaker.make

  private object CosmosDb {
    private val dbConn = System.getenv("DB_CONN_ENDPOINT")
    val dbClient: CosmosAsyncClient = new CosmosClientBuilder()
      .endpoint(dbConn)
      .key(System.getenv("DB_CONN_KEY"))
      .buildAsyncClient()
  }

  @FunctionName("ScalaFunction")
  def run(@BlobTrigger(connection = "storageConn", dataType = "", name = "myblob", path = "blobs/{name}") myblob: String,
          @BindingName("name") fileName: String,
           context: ExecutionContext): Unit = {


    context.getLogger.info("Scala trigger processed a request.")
    logger.info(s"CosmosDb: ${CosmosDb.dbClient == null}")
    logger.info(s"Foobar1338: $fileName")

    val container = CosmosDb.dbClient.getDatabase("blobfunctionsdb").getContainer("blobfunctionsContainer1")
    val upsert = container.upsertItem(writeToString(FileMetadata(name = fileName, timestamp = LocalDateTime.now())))

    logger.info(s"upsert statuscode: ${Try(upsert.block().getStatusCode).getOrElse("unknown")}")
  }
}
