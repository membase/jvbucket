/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import java.util.List;

/**
 * @author Eugene Shelestovich
 */
public interface Config {

    // Config access

    int getReplicasCount();

    int getVbucketsCount();

    int getServersCount();

    HashAlgorithm getHashAlgorithm();

    String getServer(int serverIndex);

    // VBucket access

    int getVbucketByKey(Object key, int nkey);

    int getMaster(int vbucketIndex);

    int getReplica(int vbucketIndex, int replicaIndex);

    void setServers(List<String> servers);

    void setVbuckets(List<VBucket> vbuckets);

}
