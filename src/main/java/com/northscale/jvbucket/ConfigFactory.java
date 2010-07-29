/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import org.codehaus.jettison.json.JSONObject;

/**
 * @author Eugene Shelestovich
 */
public interface ConfigFactory {

    Config createConfigFromFile(String filename);

    Config createConfigFromString(String data);

    Config createConfigFromJSON(JSONObject jsonObject);
}
