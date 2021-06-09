package com.eshop.bazar.services;

import com.eshop.bazar.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iot.model.CannedAccessControlList;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3Service {

    @Autowired
    ImageService imageService;

    //Name of the bucket on S3 storage
    String bucketName = "bazar.images";


    S3Client s3Client;

    public AwsS3Service(){
        init();
    }

    private void init(){
        //Region where bucket is configured on. It will not connect if you not set correct Region

        Region region = Region.US_EAST_2;

        /*
         * Here we are using environment credentials provide
         * do not forget to set environment variables on the terminal
         * export AWS_ACCESS_KEY_ID=xxxxx
         * export AWS_SECRET_ACCESS_KEY=xxxxx
         */
        s3Client = S3Client
                .builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }


    public void upload(MultipartFile file){
        String key = file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .acl(String.valueOf(CannedAccessControlList.PUBLIC_READ))
                .bucket(bucketName)
                .key(key)
                .build();

        //before putting to cloud convert multipartFile to java File
        s3Client.putObject(objectRequest,
                RequestBody.fromFile(convertFromMultipart(file)));

        //Save image object into DB
        Image image = new Image();
        image.setName(key);
        image.setLink("https://s3.us-east-2.amazonaws.com/bazar.images/"+key);
        imageService.create(image);
        //deleting file right after uploading
        try {
        Files.delete(Paths.get(file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * call method to Retrieve s3objects and create Image objects
     * the public link differs according to name of the bucket and Region
     */
    public List<Image> listAllImages(){
        List<S3Object> s3ObjectList = listBucketObjects(s3Client, bucketName);
        return s3ObjectList
                .stream()
                .map(s3Object -> {
                    Image image = new Image();
                    image.setName(s3Object.key());
                    image.setLink("https://s3.us-east-2.amazonaws.com/bazar.images/"+s3Object.key());
                    return image;
                }).collect(Collectors.toList());
    }

    /*
     * this method is where s3objects are being retrieved
     */
    private List<S3Object> listBucketObjects(S3Client s3Client, String bucketName) {
        try {
            ListObjectsRequest listObjectsRequest = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
            return listObjectsResponse.contents();
        } catch (S3Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String key){
        DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        imageService.deleteImage(key);

    }

    /*
     * the utility function to convert Spring multipartFile to Java File
     */
    private File convertFromMultipart(MultipartFile multipartFile){
        File convertedFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedFile;

    }
}
