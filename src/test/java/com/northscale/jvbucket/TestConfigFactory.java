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
import net.spy.memcached.HashAlgorithm;
import org.apache.commons.collections15.CollectionUtils;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Eugene Shelestovich
 */
public class TestConfigFactory {

    private static final String config =
            "{\n" +
                    "  \"hashAlgorithm\": \"CRC\",\n" +
                    "  \"numReplicas\": 2,\n" +
                    "  \"serverList\": [\"server1:11211\", \"server2:11210\", \"server3:11211\"],\n" +
                    "  \"vBucketMap\":\n" +
                    "    [\n" +
                    "      [0, 1, 2],\n" +
                    "      [1, 2, 0],\n" +
                    "      [2, 1, -1],\n" +
                    "      [1, 2, 0]\n" +
                    "    ]\n" +
                    "}";

    private static final String configFlat =
            "{" +
                    "  \"hashAlgorithm\": \"CRC\"," +
                    "  \"numReplicas\": 2," +
                    "  \"serverList\": [\"server1:11211\", \"server2:11210\", \"server3:11211\"]," +
                    "  \"vBucketMap\":" +
                    "    [" +
                    "      [0, 1, 2]," +
                    "      [1, 2, 0]," +
                    "      [2, 1, -1]," +
                    "      [1, 2, 0]" +
                    "    ]" +
                    "}";

    private static final String configInEnvelope =
            "{ \"otherKeyThatIsIgnored\": 12345,\n" +
                    "\"vBucketServerMap\": \n" +
                    "{\n" +
                    "  \"hashAlgorithm\": \"CRC\",\n" +
                    "  \"numReplicas\": 2,\n" +
                    "  \"serverList\": [\"server1:11211\", \"server2:11210\", \"server3:11211\"],\n" +
                    "  \"vBucketMap\":\n" +
                    "    [\n" +
                    "      [0, 1, 2],\n" +
                    "      [1, 2, 0],\n" +
                    "      [2, 1, -1],\n" +
                    "      [1, 2, 0]\n" +
                    "    ]\n" +
                    "}" +
                    "}";

    private final String cfg1 = "{\n" +
            "  \"hashAlgorithm\": \"CRC\",\n" +
            "  \"numReplicas\": 2,\n" +
            "  \"serverList\": [\"server1:11211\", \"server2:11210\", \"server3:11211\"],\n" +
            "  \"vBucketMap\":\n" +
            "    [\n" +
            "      [0, 1, 2],\n" +
            "      [1, 2, 0],\n" +
            "      [2, 1, -1],\n" +
            "      [1, 2, 0]\n" +
            "    ]\n" +
            "}";

    private final String cfg2 = "{\n" +
            "  \"hashAlgorithm\": \"CRC\",\n" +
            "  \"numReplicas\": 2,\n" +
            "  \"serverList\": [\"server1:11211\", \"server2:11210\", \"server4:11211\"],\n" +
            "  \"vBucketMap\":\n" +
            "    [\n" +
            "      [0, 1, 2],\n" +
            "      [1, 2, 0],\n" +
            "      [2, 1, -1],\n" +
            "      [0, 2, 0]\n" +
            "    ]\n" +
            "}";

    private final String cfg3 = "{\n" +
            "  \"hashAlgorithm\": \"CRC\",\n" +
            "  \"numReplicas\": 1,\n" +
            "  \"serverList\": [\"server1:11211\", \"server2:11210\"],\n" +
            "  \"vBucketMap\":\n" +
            "    [\n" +
            "      [0, 1],\n" +
            "      [1, 0],\n" +
            "      [1, 0],\n" +
            "      [0, 1],\n" +
            "      [0, 1],\n" +
            "      [1, 0],\n" +
            "      [1, 0],\n" +
            "      [0, 1]\n" +
            "    ]\n" +
            "}";

    private static final String filename = "test.json";

    private void doTest(String json) {
        ConfigFactory cf = new DefaultConfigFactory();
        Config config = cf.createConfigFromString(json);
        validate(config);
    }

    private void doTestWithFile(String filename) {
        ConfigFactory cf = new DefaultConfigFactory();
        Config config = cf.createConfigFromFile(filename);
        validate(config);
    }

    private void validate(Config config) {
        assertEquals(3, config.getServersCount());
        assertEquals(2, config.getReplicasCount());
        assertEquals(4, config.getVbucketsCount());
        assertEquals(HashAlgorithm.CRC32_HASH, config.getHashAlgorithm());

        assertEquals("server1:11211", config.getServer(0));
        assertEquals("server2:11210", config.getServer(1));
        assertEquals("server3:11211", config.getServer(2));

        assertEquals(0, config.getMaster(0));
        assertEquals(1, config.getMaster(1));
        assertEquals(2, config.getMaster(2));
        assertEquals(1, config.getMaster(3));

        assertEquals(2, config.getReplica(0, 1));
        assertEquals(2, config.getReplica(1, 0));
        assertEquals(1, config.getReplica(0, 0));
    }

    @Test
    public void testConfig() {
        doTest(config);
    }

    @Test
    public void testConfigFlat() {
        doTest(configFlat);
    }

    @Test
    public void testConfigInEnvelope() {
        doTest(configInEnvelope);
    }

    @Test
    public void testConfigFromFile() {
        URL url = this.getClass().getClassLoader().getResource(filename);
        doTestWithFile(url.getFile());
    }

    @Test
    public void testCompareTo() {
        ConfigFactory cf = new DefaultConfigFactory();
        Config config1 = cf.createConfigFromString(cfg1);
        Config config2 = cf.createConfigFromString(cfg2);
        ConfigDifference diff = config1.compareTo(config2);
        assertTrue(diff.isSequenceChanged());
        assertEquals(1, diff.getVbucketsChanges());
        assertEquals("server4:11211", diff.getServersAdded().get(0));
        assertEquals("server3:11211", diff.getServersRemoved().get(0));

        config2 = cf.createConfigFromString(cfg3);
        diff = config1.compareTo(config2);
        assertTrue(diff.isSequenceChanged());
        assertEquals(-1, diff.getVbucketsChanges());
        assertTrue(diff.getServersAdded().isEmpty());
        assertEquals("server3:11211", diff.getServersRemoved().get(0));
    }

    @Test
    public void testWrongServer() {
        ConfigFactory cf = new DefaultConfigFactory();
        Config cfg = cf.createConfigFromString(config);
        // Starts at 0
        assertEquals(0, cfg.getMaster(0));
        // Does not change when I told it I found the wrong thing
        assertEquals(0, cfg.foundIncorrectMaster(0, 1));
        assertEquals(0, cfg.getMaster(0));
        // Does change if I tell it I got the right thing and it was wrong.
        assertEquals(1, cfg.foundIncorrectMaster(0, 0));
        assertEquals(1, cfg.getMaster(0));
        // ...and again
        assertEquals(2, cfg.foundIncorrectMaster(0, 1));
        assertEquals(2, cfg.getMaster(0));
        // ...and then wraps
        assertEquals(0, cfg.foundIncorrectMaster(0, 2));
        assertEquals(0, cfg.getMaster(0));
    }
}
