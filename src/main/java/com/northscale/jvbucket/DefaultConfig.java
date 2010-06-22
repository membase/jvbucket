/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Eugene Shelestovich
 */
public class DefaultConfig implements Config {

    private final Log LOG = LogFactory.getLog(this.getClass());

    private HashAlgorithm hashAlgorithm = HashAlgorithm.DEFAULT;

    private int vbucketsCount;

    private int mask;

    private int serversCount;

    private int replicasCount;

    private List<String> servers;

    private List<VBucket> vbuckets;

    public DefaultConfig(HashAlgorithm hashAlgorithm, int serversCount, int replicasCount, int vbucketsCount) {
        this.hashAlgorithm = hashAlgorithm;
        this.serversCount = serversCount;
        this.replicasCount = replicasCount;
        this.vbucketsCount = vbucketsCount;
        this.mask = vbucketsCount - 1;
    }

    public int getReplicasCount() {
        return replicasCount;
    }

    public int getVbucketsCount() {
        return vbucketsCount;
    }

    public int getServersCount() {
        return serversCount;
    }

    public String getServer(int serverIndex) {
        if (serverIndex > servers.size() - 1) {
            throw new IllegalArgumentException("Server index is out of bounds, index = "
                    + serverIndex + ", servers count = " + servers.size());
        }
        return servers.get(serverIndex);
    }

    public int getVbucketByKey(Object key, int nkey) {
        // TODO
        int digest = 0;
        return digest & mask;
    }

    public int getMaster(int vbucketIndex) {
        if (vbucketIndex > vbuckets.size() - 1) {
            throw new IllegalArgumentException("Vbucket index is out of bounds, index = "
                    + vbucketIndex + ", vbuckets count = " + vbuckets.size());
        }
        return vbuckets.get(vbucketIndex).getServers()[0];
    }

    public int getReplica(int vbucketIndex, int replicaIndex) {
        if (vbucketIndex > vbuckets.size() - 1) {
            throw new IllegalArgumentException("Vbucket index is out of bounds, index = "
                    + vbucketIndex + ", vbuckets count = " + vbuckets.size());
        }
        return vbuckets.get(vbucketIndex).getServers()[replicaIndex + 1];
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public void setVbuckets(List<VBucket> vbuckets) {
        this.vbuckets = vbuckets;
    }

    public HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }
}
