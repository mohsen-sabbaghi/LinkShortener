package ir.bki.filters;

import org.apache.log4j.Logger;
import org.joda.time.YearMonth;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.atomic.AtomicInteger;

import static ir.bki.logging.LoggerTypes.REQUESTS;
import static ir.bki.logging.LoggerTypes.STACK_TRACE;


/**
 * Created by me-sharifi on 02/11/2019.
 */
@Provider
@LogReqRes
@Priority(100)
public class RequestResponseLoggerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final AtomicInteger requestCounter = new AtomicInteger(0);
    private static final ThreadLocal<StopWatch> threadLocalStopWatch = new ThreadLocal<>();

    private final Logger LOGGER_REQUESTS = Logger.getLogger(REQUESTS);
    private final Logger LOGGER_STACKTRACE = Logger.getLogger(STACK_TRACE);
    @Context
    private ResourceInfo resourceInfo;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            threadLocalStopWatch.set(new Log4JStopWatch());
            int requestCounterInt = requestCounter.incrementAndGet();
            String serverRequestCounter = new YearMonth().monthOfYear().get() + String.format("%0" + 9 + "d", requestCounterInt);
            servletRequest.setAttribute("SERVER_REQUEST_COUNTER", serverRequestCounter);
            LOGGER_REQUESTS.info(serverRequestCounter + " -->*GET  REQ  : " + httpHeaders.getRequestHeaders().toString());
        } catch (Exception ex) {
            LOGGER_STACKTRACE.error("Exception in " + RequestResponseLoggerFilter.class.getSimpleName() + " **Request** ,message: " + ex);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
//        MultivaluedMap<String, Object> map = responseContext.getHeaders();
//        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
//            System.out.println("Key : " + entry.getKey() +
//                    " ,Value : " + entry.getValue());
//        }
        try {
            String serverRequestCounter = (String) servletRequest.getAttribute("SERVER_REQUEST_COUNTER");
            StopWatch stopwatch = threadLocalStopWatch.get();
            stopwatch.stop("Log Request");
            LOGGER_REQUESTS.info(serverRequestCounter + " <-- SEND RES  : "  + responseContext.getHeaderString("short_link"));
        } catch (Exception ex) {
            LOGGER_STACKTRACE.error("Exception in " + RequestResponseLoggerFilter.class.getSimpleName() + " **Response** ,message: " + ex);
        }
    }

}
