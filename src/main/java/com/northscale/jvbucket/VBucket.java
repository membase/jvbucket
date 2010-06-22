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
public class VBucket {

    private final static int MAX_REPLICAS = 4;

    private int[] servers = new int[MAX_REPLICAS + 1];

    public int[] getServers() {
        return servers;
    }
}
