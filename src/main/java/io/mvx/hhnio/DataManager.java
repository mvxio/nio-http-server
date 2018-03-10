package io.mvx.hhnio;

import io.mvx.hhnio.utils.PropertiesFactory;

import java.nio.MappedByteBuffer;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

class DataManager {

    private class CacheItem {
        private long timestamp;
        private MappedByteBuffer fileContent;
    }

    private final Map<String, CacheItem> cache = new HashMap<>();
    private String root = ".";
    private boolean caching= true;

    DataManager() {
        try {
            Properties props = PropertiesFactory.load();
            root = Optional.ofNullable(props.getProperty("root")).orElse(".");
            caching = Optional.ofNullable(props.getProperty("caching")).orElse("true").equalsIgnoreCase("true");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    MappedByteBuffer getFile(String uri) throws IOException {
        File file = new File(root + uri);
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            throw new IOException();
        }

        CacheItem cacheItem = cache.get(uri);
        if (cacheItem == null) {
            cacheItem = new CacheItem();
            if (caching) {
                cache.put(uri, cacheItem);
            }
        }

        if (file.lastModified() > cacheItem.timestamp) {
            // Create the set of options for reading the file.
            Set<OpenOption> options = new HashSet<>();
            options.add(StandardOpenOption.READ);
            // File path.
            Path path = file.toPath();
            // Reading file.
            FileChannel fileChannel = (FileChannel) Files.newByteChannel(path, options);
            cacheItem.fileContent = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            cacheItem.timestamp = file.lastModified();
            fileChannel.close();
        } else {
            cacheItem.fileContent.rewind();
        }

        return cacheItem.fileContent;
    }

}
