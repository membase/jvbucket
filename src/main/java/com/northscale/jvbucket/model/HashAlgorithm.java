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
public enum HashAlgorithm {

    DEFAULT("default"),
    MD5("md5"),
    CRC("crc"),
    FNV1_64("fnv1_64"),
    FNV1A_64("fnv1a_64"),
    FNV1_32("fnv1_32"),
    FNV1A_32("fnv1a_32"),
    HSIEH("hsieh"),
    MURMUR("murmur"),
    JENKINS("jenkins");

    private String value;

    HashAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HashAlgorithm parse(String hashAlgorithm) {
        for (HashAlgorithm algorithm : HashAlgorithm.values()) {
            if (algorithm.getValue().equalsIgnoreCase(hashAlgorithm)) {
                return algorithm;
            }
        }
        return DEFAULT;
    }

    @Override
    public String toString() {
        return value;
    }
}
