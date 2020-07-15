package zycrophat.fileeventprocessor

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.{BindingName, BlobTrigger, FunctionName}
import com.typesafe.scalalogging.LazyLogging

class MyFunction extends LazyLogging {

  @FunctionName("ScalaFunction")
  def run(@BlobTrigger(connection = "storageConn", dataType = "", name = "myblob", path = "blobs/{name}") myblob: String,
          @BindingName("name") fileName: String,
           context: ExecutionContext): Unit = {

    context.getLogger.info("Scala trigger processed a request.")
    logger.info(s"Foobar1337: $fileName")
  }
}
