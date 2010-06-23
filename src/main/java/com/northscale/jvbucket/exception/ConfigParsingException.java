/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket.exception;

/**
 * @author Eugene Shelestovich
 */
public class ConfigParsingException extends RuntimeException {

    public ConfigParsingException() {
        super();
    }

    public ConfigParsingException(String message) {
        super(message);
    }

    public ConfigParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigParsingException(Throwable cause) {
        super(cause);
    }
}
