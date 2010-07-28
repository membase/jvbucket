/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import com.northscale.jvbucket.model.ConfigDifference;
import com.northscale.jvbucket.model.VBucket;
import net.spy.memcached.HashAlgorithm;
import org.apache.commons.collections15.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Shelestovich
 */
public class DefaultConfig implements Config {

    private HashAlgorithm hashAlgorithm = HashAlgorithm.NATIVE_HASH;

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

    public int getVbucketByKey(String key) {
        int digest = (int) hashAlgorithm.hash(key);
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

    public int foundIncorrectMaster(int vbucket, int wrongServer) {
        int mappedServer = this.vbuckets.get(vbucket).getServers()[0];
        int rv = mappedServer;
        if (mappedServer == wrongServer) {
            rv = (rv + 1) % this.serversCount;
            this.vbuckets.get(vbucket).getServers()[0] = rv;
        }
        return rv;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public void setVbuckets(List<VBucket> vbuckets) {
        this.vbuckets = vbuckets;
    }

    public List<String> getServers() {
        return servers;
    }

    public List<VBucket> getVbuckets() {
        return vbuckets;
    }

    public ConfigDifference compareTo(Config config) {
        ConfigDifference difference = new ConfigDifference();

        // Compute the added and removed servers 
        difference.setServersAdded(new ArrayList<String>(CollectionUtils.subtract(config.getServers(), this.getServers())));
        difference.setServersRemoved(new ArrayList<String>(CollectionUtils.subtract(this.getServers(), config.getServers())));

        // Verify the servers are equal in their positions
        if (this.serversCount == config.getServersCount()) {
            difference.setSequenceChanged(false);
            for (int i = 0; i < this.serversCount; i++) {
                if (!this.getServer(i).equals(config.getServer(i))) {
                    difference.setSequenceChanged(true);
                    break;
                }
            }
        } else {
            // Just say yes
            difference.setSequenceChanged(true);
        }

        // Count the number of vbucket differences
        if (this.vbucketsCount == config.getVbucketsCount()) {
            int vbucketsChanges = 0;
            for (int i = 0; i < this.vbucketsCount; i++) {
                vbucketsChanges += (this.getMaster(i) == config.getMaster(i)) ? 0 : 1;
            }
            difference.setVbucketsChanges(vbucketsChanges);
        } else {
            difference.setVbucketsChanges(-1);
        }

        return difference;
    }

    public HashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }


}
