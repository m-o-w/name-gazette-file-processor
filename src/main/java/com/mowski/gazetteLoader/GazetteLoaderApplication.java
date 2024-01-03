package com.mowski.gazetteLoader;

import com.mowski.gazetteLoader.service.PdfFileWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GazetteLoaderApplication implements CommandLineRunner {

	@Autowired
	private PdfFileWatcher pdfFileWatcher;

	public static void main(String[] args) {
		SpringApplication.run(GazetteLoaderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		pdfFileWatcher.startFileWatcher();
	}
}
