/*
 * Copyright 2015 psychocoder.
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
package net.letshackit.psychohasher;

/**
 * Enum that contains all the HashType Algo types supported by this application.
 * 
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public enum HashType {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private final String value;

    private HashType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static String[] getSupportedHashes() {
        return new String[] {"MD5", "SHA1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"};
    }
    
}
