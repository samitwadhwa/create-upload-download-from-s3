package org.example;
import java.io.*;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;


public class Main {
    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("samits_file.json"));

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = input.read(buffer)) !=  -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        input.close();
        outputStream.close();
    }
    public static void main(String[] args) throws IOException {
        String accessKeyID = "AKIAWNO6I7HEY4YX7SF6";// You get it form AWS Console
        String secretAccessKey = "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"; // You get it form AWS Console
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);
        // 0. Create a S3 Client
        AmazonS3 s3client = new AmazonS3Client(awsCredentials);

        // 1. Get all bucket names.
        s3client.listBuckets();
        for (Bucket bucket : s3client.listBuckets()) {
            System.out.println("bucketName=" + bucket.getName());
        }

        System.out.println("---------bucketName Done--------------");

//         2. create bucket
        String bucketName = "testcreatedbucket";
        s3client.createBucket(bucketName);
        System.out.println("---------Create Bucket Done--------------");

//         3. Create a folder in a bucket.
        String folderName = "samitBucket";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/", emptyContent, metadata);
        s3client.putObject(putObjectRequest);
        System.out.println("---------Create Folder Done--------------");

        // 4  Download a folder
        String bucket_name = "testbucketwithkeys";
        String key = "samits_file.json";
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucket_name, key);
        S3Object objectPortion = s3client.getObject(rangeObjectRequest);
        System.out.println("Printing bytes retrieved.");
        displayTextInputStream(objectPortion.getObjectContent());
        }
    }
