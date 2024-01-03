package com.mowski.gazetteLoader.service;

import com.mowski.gazetteLoader.model.NameGazetteRecord;
import com.mowski.gazetteLoader.repository.NameGazetteRecordRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NameGazetteFileProcessor implements FileProcessor {

    @Autowired
    private NameGazetteRecordRepository gazetteRecordRepository;

    @Value("${gazette.log.size}")
    int maxLength;
    @Value("${gazette.filename.regex}")
    String fileNameRegex;
    @Value("${gazette.text.delimiter}")
    String pdfTextDelimiter;
    String pdfText;
    String year;
    String fileName;
    String issueDate;

    @Override
    public void process(File file) {

        fileName = file.getName();
        issueDate = fileName.replace(".pdf", "");
        // Check if file is already processed
        boolean issueDateExists = checkFileAlreadyProcessed(issueDate);
        // Process file
        if (!issueDateExists) {
            pdfText = getFileText(file);
            year    = getFileYear(fileName);

            System.out.println("Processing started for file name: " + fileName);
            System.out.println("File text length is: " + pdfText.length());

            String[] gazetteLogs = parseGazetteLogs(pdfText);
            System.out.println("Loaded gazette logs count: " + Arrays.stream(gazetteLogs).count());
            insertGazetteLogsToDb(gazetteLogs);
            System.out.println("Processing complete for file name: " + fileName);
        }
        else {
            System.out.println("Already processed file: " + fileName);
            log.info("Already processed file: " + fileName);
        }
    }

    private void insertGazetteLogsToDb(String[] gazetteLogs) {
        for(String gazetteLog: gazetteLogs) {
            NameGazetteRecord record = new NameGazetteRecord();
            record.setIssueDate(issueDate);
            record.setYear(year);
            record.setDataType("Name Gazette");
            record.setPublishedLog(gazetteLog);

            System.out.println("Saving: " + record);
            gazetteRecordRepository.save(record);
        }
    }

    private String[] parseGazetteLogs(String pdfText) {
        ArrayList<String> gazetteLogs = new ArrayList<>();
        Pattern patternDelimiter = Pattern.compile(pdfTextDelimiter);
        System.out.println("Delimiter is: "+pdfTextDelimiter);
        Matcher matcherDelimiter = patternDelimiter.matcher(pdfText);

        int lastIndex = 0;
        while (matcherDelimiter.find()) {
            String token = pdfText.substring(lastIndex, matcherDelimiter.end());
            lastIndex = matcherDelimiter.end();

            String originalLog = token.trim(); // Replace this with your actual string
            String trimmedLog = originalLog.length() > maxLength ? originalLog.substring(originalLog.length() - maxLength) : originalLog;
            gazetteLogs.add(trimmedLog);
        }
        System.out.println("Gazette Array size: " + gazetteLogs.size());
        return gazetteLogs.toArray(new String[0]);
    }

    private String getFileYear(String fileName) {
        // Use the regex pattern to match the specified format
        Pattern pattern = Pattern.compile(fileNameRegex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.matches())
            return matcher.group(1);
        else
            throw new IllegalArgumentException("File name does not match the expected pattern");
    }

    private String getFileText(File file) {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkFileAlreadyProcessed(String issueDate) {
        return gazetteRecordRepository.existsByIssueDate(issueDate);
    }
}
