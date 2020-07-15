package zycrophat.fileeventprocessor

import java.time.LocalDateTime

import com.azure.cosmos.{CosmosAsyncClient, CosmosClientBuilder}
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.{BindingName, BlobTrigger, FunctionName}
import com.typesafe.scalalogging.LazyLogging
import org.json4s._
import org.json4s.ext.JavaTimeSerializers
import org.json4s.native.Serialization.{read, write}



class MyFunction extends LazyLogging {
  private implicit val formats: Formats = DefaultFormats ++ JavaTimeSerializers.all

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
    logger.info(s"Foobar1337: $fileName")

    val container = CosmosDb.dbClient.getDatabase("blobfunctionsdb").getContainer("blobfunctionsContainer1")
    val upsert = container.upsertItem(write(FileMetadata(name = fileName, timestamp = LocalDateTime.now())))

    logger.info(s"upsert statuscode: ${upsert.block().getStatusCode}")
  }
}
