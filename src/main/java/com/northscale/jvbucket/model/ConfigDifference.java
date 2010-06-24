/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket.model;

import java.util.List;

/**
 * @author Eugene Shelestovich
 */
public class ConfigDifference {

    /**
     * List of server names that were added.
     */
    private List<String> serversAdded;

    /**
     * List of server names that were removed.
     */
    private List<String> serversRemoved;

    /**
     * Number of vbuckets that changed.  -1 if the total number changed.
     */
    private int vbucketsChanges;

    /**
     * True if the sequence of servers changed.
     */
    private boolean sequenceChanged;

    public List<String> getServersAdded() {
        return serversAdded;
    }

    public void setServersAdded(List<String> serversAdded) {
        this.serversAdded = serversAdded;
    }

    public List<String> getServersRemoved() {
        return serversRemoved;
    }

    public void setServersRemoved(List<String> serversRemoved) {
        this.serversRemoved = serversRemoved;
    }

    public int getVbucketsChanges() {
        return vbucketsChanges;
    }

    public void setVbucketsChanges(int vbucketsChanges) {
        this.vbucketsChanges = vbucketsChanges;
    }

    public boolean isSequenceChanged() {
        return sequenceChanged;
    }

    public void setSequenceChanged(boolean sequenceChanged) {
        this.sequenceChanged = sequenceChanged;
    }
}
