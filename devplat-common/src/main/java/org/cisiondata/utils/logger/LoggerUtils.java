package org.cisiondata.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
	
	private static Logger LOG_WARN = LoggerFactory.getLogger("WARN_LOG");
	private static Logger LOG_DEBUG = LoggerFactory.getLogger("DEBUG_LOG");
	private static Logger LOG_INFO = LoggerFactory.getLogger("INFO_LOG");
	private static Logger LOG_ERROR = LoggerFactory.getLogger("ERROR_LOG");

	/**
	 * WARN LOG
	 * @param message
	 * @param thrown
	 */
	public static void warn(String message, Throwable throwable) {
		LOG_WARN.warn(message, throwable);
	}
	
	/**
	 * DEBUG LOG
	 * @param message
	 * @param thrown
	 */
	public static void debug(String message, Throwable throwable) {
		LOG_DEBUG.debug(message, throwable);
	}
	
	/**
	 * INFO LOG
	 * @param message
	 * @param thrown
	 */
	public static void info(String message, Throwable throwable) {
		LOG_INFO.info(message, throwable);
	}
	
	/**
	 * ERROR LOG
	 * @param message
	 * @param throwable
	 */
	public static void error(String message, Throwable throwable) {
		LOG_ERROR.error(message, throwable);
	}
	
}
