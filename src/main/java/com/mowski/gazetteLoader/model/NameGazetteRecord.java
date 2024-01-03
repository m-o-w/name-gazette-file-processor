package com.mowski.gazetteLoader.model;

import jakarta.persistence.*;

@Entity
@Table(name = "NameGazetteRecords")
public class NameGazetteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IssueDate")
    private String issueDate;

    @Column(name = "Year")
    private String year;

    @Column(name = "DataType")
    private String dataType;

    @Column(name = "PublishedLog", length = 1000)
    private String publishedLog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPublishedLog() {
        return publishedLog;
    }

    public void setPublishedLog(String publishedLog) {
        this.publishedLog = publishedLog;
    }

    @Override
    public String toString() {
        return "NameGazetteRecord{" +
                "id=" + id +
                ", issueDate='" + issueDate + '\'' +
                ", year='" + year + '\'' +
                ", dataType='" + dataType + '\'' +
                ", publishedLog='" + publishedLog.length() + '\'' +
                '}';
    }
}
