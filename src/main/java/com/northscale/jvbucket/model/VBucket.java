/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket.model;

/**
 * @author Eugene Shelestovich
 */
public class VBucket {

    public final static int MAX_REPLICAS = 4;

    public final static int MAX_BUCKETS = 65536;

    private int[] servers = new int[MAX_REPLICAS + 1];

    public int[] getServers() {
        return servers;
    }
}
