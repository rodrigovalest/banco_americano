package com.rodrigovalest.ms_costumer.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.rodrigovalest.ms_costumer.exceptions.FileConvertionException;
import com.rodrigovalest.ms_costumer.exceptions.FileSizeException;
import com.rodrigovalest.ms_costumer.exceptions.AWSErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
public class AWSService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Instance;

    public String upload(String base64Photo) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Photo);

        if (decodedBytes.length > 5 * 1024 * 1024)
            throw new FileSizeException("File size exceeds the maximum limit of 5MB");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(decodedBytes.length);

        String filename = System.currentTimeMillis() + "__" + UUID.randomUUID().toString() + ".jpg";

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
            this.s3Instance.putObject(
                    new PutObjectRequest(this.bucketName, filename, inputStream, metadata)
            );
            URL s3Url = s3Instance.getUrl(bucketName, filename);
            return s3Url.toString();
        } catch (IOException e) {
            log.info("ERROR: IOException: {}", e.getMessage());
            throw new FileConvertionException("ERROR trying to upload base64 file to S3: " + e.getMessage());
        } catch (AmazonS3Exception e) {
            log.info("ERROR: Amazon Service Exception: {}", e.getMessage());
            throw new AWSErrorException("ERROR: Amazon Service Exception: {" + e.getMessage() + "}");
        } catch (SdkClientException e) {
            log.error("ERROR: SDK Client Exception: {}", e.getMessage());
            throw new AWSErrorException("SDK Client Exception: " + e.getMessage());
        }
    }

    public String download(String urlPhoto) {
        String filename = urlPhoto.substring(urlPhoto.lastIndexOf("/") + 1);

        S3Object s3Object = s3Instance.getObject(bucketName, filename);
        try (InputStream inputStream = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("ERROR trying to download and convert file to base64: {}", e.getMessage());
            throw new FileConvertionException("ERROR trying to download and convert file to base64: " + e.getMessage());
        }
    }

    public String update(String urlPhoto, String base64photo) {
        String filename = urlPhoto.substring(urlPhoto.lastIndexOf("/") + 1);

        try {
            this.s3Instance.deleteObject(this.bucketName, filename);
        } catch (AmazonS3Exception e) {
            log.error("ERROR: Amazon Service Exception when deleting old file: {}", e.getMessage());
            throw new AWSErrorException("ERROR: Amazon Service Exception when deleting old file: {" + e.getMessage() + "}");
        } catch (SdkClientException e) {
            log.error("ERROR: SDK Client Exception when deleting old file: {}", e.getMessage());
            throw new AWSErrorException("SDK Client Exception when deleting old file: " + e.getMessage());
        }

        return this.upload(base64photo);
    }

    public void delete(String urlPhoto) {
        String filename = urlPhoto.substring(urlPhoto.lastIndexOf("/") + 1);

        try {
            this.s3Instance.deleteObject(this.bucketName, filename);
        } catch (AmazonS3Exception e) {
            log.error("ERROR: Amazon Service Exception when deleting file: {}", e.getMessage());
            throw new AWSErrorException("ERROR: Amazon Service Exception when deleting old file: {" + e.getMessage() + "}");
        } catch (SdkClientException e) {
            log.error("ERROR: SDK Client Exception when deleting file: {}", e.getMessage());
            throw new AWSErrorException("SDK Client Exception when deleting old file: " + e.getMessage());
        }
    }
}
