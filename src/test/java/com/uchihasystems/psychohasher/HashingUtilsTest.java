/*
 * Copyright 2016 psychocoder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uchihasystems.psychohasher;

import java.io.File;
import java.net.URL;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author psychocoder
 */
public class HashingUtilsTest {

    private final URL testFile;

    public HashingUtilsTest() {
        testFile = this.getClass().getClassLoader().getResource("files/Test1");
    }

    @Test
    public void testStringMD5Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.MD5), "b10a8db164e0754105b7a99be72e3fe5");
    }

    @Test
    public void testFileMD5Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.MD5), "4b2bac65358fcafc5fa5802b42e9"
                + "cfa2");
    }

    @Test
    public void testStringSHA1Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.SHA1), "0a4d55a8d778e5022fab701977c5d840bbc486d0");
    }

    @Test
    public void testFileSHA1Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.SHA1), "71c43fe5135f5d3287269b54102"
                + "b4db43d3f63af");
    }

    @Test
    public void testStringSHA224Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.SHA224), "c4890faffdb0105d991a461e668e276685401b0"
                + "2eab1ef4372795047");
    }

    @Test
    public void testFileSHA224Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.SHA224), "fc6120b51f8de231489eccb09"
                + "cef6fca6f7d0051918192058d4d4917");
    }

    @Test
    public void testStringSHA256Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.SHA256), "a591a6d40bf420404a011733cfb7b190d62c65b"
                + "f0bcda32b57b277d9ad9f146e");
    }

    @Test
    public void testFileSHA256Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.SHA256), "cbd6e7cca8594d3e2895b627"
                + "d36eaa03c2eca29983841349a754e84cc3953623");
    }

    @Test
    public void testStringSHA384Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.SHA384), "99514329186b2f6ae4a1329e7ee6c610a72963"
                + "6335174ac6b740f9028396fcc803d0e93863a7c3d90f86beee782f4f3f");
    }

    @Test
    public void testFileSHA384Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.SHA384), "bba5ff5257183de27d030d665"
                + "7066745495a7d7ca05ac5205915cc5237db41db0c4907827219c5a8e14cb9e15c57dd8a");
    }

    @Test
    public void testStringSHA512Hash() {
        assertEquals(HashingUtils.getHash("Hello World", HashType.SHA512), "2c74fd17edafd80e8447b0d46741ee243b7eb74"
                + "dd2149a0ab1b9246fb30382f27e853d8585719e0e67cbda0daa8f51671064615d645ae27acb15bfb1447f459b");
    }

    @Test
    public void testFileSHA512Hash() {
        assertEquals(HashingUtils.getFileHash(new File(testFile.getFile()), HashType.SHA512), "d01fe32cbfc361e5bbcb0be54"
                + "abeac615b90c57ef73da55d1d0ab33c5566d9a64442b95062c3ebfb464c283dfb0e7a3df7bbfcb8a4f790c6a2e0d6fe"
                + "faf5ace4");
    }
}
