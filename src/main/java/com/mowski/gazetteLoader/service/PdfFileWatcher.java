package com.mowski.gazetteLoader.service;

import org.apache.pdfbox.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfFileWatcher implements FileWatcher {

    @Value("${inbound.directory}")
    private String inboundDirectory;
    @Autowired
    private FileProcessor nameGazetteFileProcessor;

    public void startFileWatcher() throws Exception {
        Path directory = Paths.get(inboundDirectory);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    Path filePath = directory.resolve((Path) event.context());
                    if (filePath.toString().endsWith(".pdf")) {
                        nameGazetteFileProcessor.process(filePath.toFile());
                        deleteFile(filePath);
                    }
                }
            }
            watchKey.reset();
        }
    }

    private void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
            System.out.println("File deleted: " + filePath.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}