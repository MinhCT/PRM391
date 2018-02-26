package com.project.group2.attendancetool.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Helper class to help encoding parameters in body request
 */
public class EncodeVolleyBody {
    /**
     * Encode parameters as a map to byte[] with custom encoding type
     *
     * @param params - A map contains key and value pairs
     * @param paramsEncoding - An encoding type
     * @return - an array of byte form of the passed map
     */
    public static byte[] encodeParams(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
