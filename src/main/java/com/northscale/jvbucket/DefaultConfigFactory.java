/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import com.northscale.jvbucket.exception.ConfigParsingException;
import com.northscale.jvbucket.model.HashAlgorithm;
import com.northscale.jvbucket.model.VBucket;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Shelestovich
 */
public class DefaultConfigFactory implements ConfigFactory {

    private final Log LOG = LogFactory.getLog(this.getClass());

    public Config createConfigFromFile(String filename) {
        if (StringUtils.isBlank(filename)) {
            throw new ConfigParsingException("Filename is empty.");
        }
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            throw new ConfigParsingException("Exception reading input file: " + e.getMessage());
        }
        return createConfigFromString(sb.toString());
    }

    public Config createConfigFromString(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return parseJSON(jsonObject);
        } catch (JSONException e) {
            throw new ConfigParsingException("Exception parsing JSON data: " + e.getMessage());
        }
    }

    private Config parseJSON(JSONObject jsonObject) throws JSONException {
        // Allows clients to have a JSON envelope.
        if (jsonObject.has("vBucketServerMap")) {
            return parseJSON(jsonObject.getJSONObject("vBucketServerMap"));
        }
        HashAlgorithm hashAlgorithm = HashAlgorithm.parse(jsonObject.getString("hashAlgorithm"));
        int replicasCount = jsonObject.getInt("numReplicas");
        if (replicasCount > VBucket.MAX_REPLICAS) {
            throw new ConfigParsingException("Expected number <= " + VBucket.MAX_REPLICAS + " for replicas.");
        }
        JSONArray servers = jsonObject.getJSONArray("serverList");
        if (servers.length() <= 0) {
            throw new ConfigParsingException("Empty servers list.");
        }
        int serversCount = servers.length();
        JSONArray vbuckets = jsonObject.getJSONArray("vBucketMap");
        int vbucketsCount = vbuckets.length();
        if (vbucketsCount == 0 || (vbucketsCount & (vbucketsCount - 1)) != 0) {
            throw new ConfigParsingException("Number of buckets must be a power of two, > 0 and <= " + VBucket.MAX_BUCKETS);
        }

        Config config = new DefaultConfig(hashAlgorithm, serversCount, replicasCount, vbucketsCount);
        populateServers(config, servers);
        populateVbuckets(config, vbuckets);

        return config;
    }

    private void populateServers(Config config, JSONArray servers) throws JSONException {
        List<String> serverNames = new ArrayList<String>();
        for (int i = 0; i < servers.length(); i++) {
            String server = servers.getString(i);
            serverNames.add(server);
        }
        config.setServers(serverNames);
    }

    private void populateVbuckets(Config config, JSONArray jsonVbuckets) throws JSONException {
        List<VBucket> vBuckets = new ArrayList<VBucket>();
        for (int i = 0; i < jsonVbuckets.length(); i++) {
            JSONArray rows = jsonVbuckets.getJSONArray(i);
            VBucket vbucket = new VBucket();
            for (int j = 0; j < rows.length(); j++) {
                vbucket.getServers()[j] = rows.getInt(j);
            }
            vBuckets.add(vbucket);
        }
        config.setVbuckets(vBuckets);
    }

}
