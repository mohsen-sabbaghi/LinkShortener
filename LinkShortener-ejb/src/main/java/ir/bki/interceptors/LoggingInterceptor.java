package ir.bki.interceptors;

import ir.bki.logging.LoggerTypes;
import ir.bki.util.StringFormatHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.perf4j.log4j.Log4JStopWatch;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Interceptor
@Loggable
public class LoggingInterceptor implements Serializable {

    private final static Logger LOGGER_SERVICE_TIMING = Logger.getLogger(LoggerTypes.SERVICE_TIMING);
    private final static Logger LOGGER_STACKTRACE = Logger.getLogger(LoggerTypes.STACK_TRACE);
    private final static Logger LOGGER_CRITICAL = Logger.getLogger(LoggerTypes.CRITICAL);
    private final static Logger LOGGER = Logger.getRootLogger();

    private static long refrenceId;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {

        String clazz = ic.getMethod().getDeclaringClass().getSimpleName();
        String method = ic.getMethod().getName();
        String format = StringFormatHelper.getFormatForStackTrace(clazz, method);
        Object[] objs = ic.getParameters();
        String entityClazzName = "";
        Object parameter = null;

        if (objs.length > 0) {
            parameter = objs[0];
            entityClazzName = parameter.getClass().getSimpleName();
        }
        Log4JStopWatch stopwatch = new Log4JStopWatch();

        try {
            return ic.proceed();
        } catch (Exception ex) {
            refrenceId++;
            String refId = "refId: " + String.format("%-6s", refrenceId + "");
            String ERROR_MESSAGE = "Goto stackTrace.log for detail! message: ";
            LOGGER.error(refId + ERROR_MESSAGE + ex.getMessage());
            LOGGER_STACKTRACE.log(Level.ERROR, refId + format, ex);
            throw ex;
        } finally {
            stopwatch.stop("SRV_" + StringFormatHelper.getFormatForStatisticsTiming(clazz, method));
            long diff = stopwatch.getElapsedTime();
            String msgLog = "[" + String.format("%-6d", diff) + " ms]" + format +
                    (parameter != null ? " ,value:" + parameter : "");
            if (diff > 30000)
                LOGGER_CRITICAL.warn("SRV_" + msgLog);
            LOGGER_SERVICE_TIMING.info(msgLog);
        }
    }

}
