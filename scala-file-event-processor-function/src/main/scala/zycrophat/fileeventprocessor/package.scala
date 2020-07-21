package zycrophat
import java.time.LocalDateTime

import com.github.plokhotnyuk.jsoniter_scala.macros.transient
import com.microsoft.azure.functions.ExecutionContext
import com.typesafe.scalalogging.{CanLog, LazyLogging, Logger, LoggerTakingImplicit}
import org.slf4j.MDC


package object fileeventprocessor {
  case class FileMetadata (id: String, name: String, timestamp: LocalDateTime)

  implicit case object CanLogExecutionContext extends CanLog[ExecutionContext] {
    override def logMessage(originalMsg: String, a: ExecutionContext): String = {
      Option(a).foreach { ctx =>
        MDC.put("invocationId", ctx.getInvocationId)
        MDC.put("FunctionName", ctx.getFunctionName)
      }
      originalMsg
    }

    override def afterLog(a: ExecutionContext): Unit = {
      MDC.remove("InvocationId")
      MDC.remove("FunctionName")
    }
  }

  trait LazyLoggingTakingImplicitExecutionContext extends LazyLogging {
    @transient
    protected lazy val executionLogger: LoggerTakingImplicit[ExecutionContext] =
      Logger.takingImplicit[ExecutionContext](getClass.getName)
  }
}
