/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

/**
 * @author Eugene Shelestovich
 */
public interface ConfigFactory {

    Config createConfigFromFile(String filename);

    Config createConfigFromString(String data);

}
