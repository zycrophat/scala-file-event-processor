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
        MDC.put("InvocationId", ctx.getInvocationId)
        MDC.put("FunctionName", ctx.getFunctionName)
        ctx.getTraceContext.getAttributes.forEach { MDC.put(_,_) }
        val operationIdAndParent = ctx.getTraceContext.getTraceparent.split('-')
        MDC.put("OperationId", operationIdAndParent(1))
        MDC.put("Parent Id", operationIdAndParent(2))
      }
      originalMsg
    }

    override def afterLog(a: ExecutionContext): Unit = {
      MDC.remove("InvocationId")
      MDC.remove("FunctionName")
      a.getTraceContext.getAttributes.forEach { (k, _) => MDC.remove(k) }
      MDC.remove("OperationId")
      MDC.remove("Parent Id")
    }
  }

  trait LazyLoggingTakingImplicitExecutionContext extends LazyLogging {
    @transient
    protected lazy val executionLogger: LoggerTakingImplicit[ExecutionContext] =
      Logger.takingImplicit[ExecutionContext](getClass.getName)
  }
}
