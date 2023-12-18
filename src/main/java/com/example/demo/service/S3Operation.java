package com.example.demo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

@Service
public class S3Operation {

    @Autowired
    private AmazonS3 amazonS3;

    private final String BUCKET_NAME = "usedetails";

    @SneakyThrows
    public String uploadFile(final File file) {
        String[] fileName = file.getName().split("\\.");
        String finalFileName = fileName[0] + "_" + LocalDate.now() + "." + fileName[1];
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, finalFileName, file));
        return "SuccessFully Uploaded : " + finalFileName + " ✔";
    }

    public String deleteFile(String fileName) {
        amazonS3.deleteObject(BUCKET_NAME, fileName);
        return fileName + " removed ...";
    }
}


