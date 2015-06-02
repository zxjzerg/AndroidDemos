
package com.zxjdev.demo;

import android.os.Looper;

import java.io.File;

public class AudioLoader {

    /**
     * Container object for all of the data surrounding an image request.
     */
    public class ImageContainer {
        /**
         * The most relevant bitmap for the container. If the image was in cache, the Holder to use for the final bitmap
         * (the one that pairs to the requested URL).
         */
        private File mAudioFile;

        /** The cache key that was associated with the request */
        private final String mCacheKey;

        /** The request URL that was specified */
        private final String mRequestUrl;

        public ImageContainer(File audioFile, String requestUrl, String cacheKey) {
            mAudioFile = audioFile;
            mRequestUrl = requestUrl;
            mCacheKey = cacheKey;
        }
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }
    }

    /**
     * Creates a cache key for use with the L1 cache.
     * 
     * @param url The URL of the request.
     */
    private static String getCacheKey(String url) {
        return new StringBuilder(url.length() + 12).append(url).toString();
    }
}
