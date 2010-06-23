/*
 * Copyright (c) 2009, NorthScale, Inc.
 *
 * All rights reserved.
 *
 * info@northscale.com
 *
 */

package com.northscale.jvbucket;

import static org.junit.Assert.assertEquals;

import com.northscale.jvbucket.model.HashAlgorithm;
import org.junit.Test;

import java.net.URL;

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
        assertEquals(HashAlgorithm.CRC, config.getHashAlgorithm());

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
}
