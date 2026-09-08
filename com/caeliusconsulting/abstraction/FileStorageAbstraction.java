package com.caeliusconsulting.abstraction;

import java.util.Date;

abstract class FileStorageService {
  abstract public String getPresignedUrl(String metadata);

  abstract protected String getSignature(String data, String secretKey);

  abstract public boolean uploadFile(String key, byte[] data);

  abstract public byte[] downloadFile(String key);
}

class S3Bucket extends FileStorageService {
  private final String url = "https://api.amazonwebservices.com/files";
  private final String secretKey = "aenansh-mittal";

  public String getPresignedUrl(String metadata) {
    String key = getSignature(metadata, secretKey);
    String uploadurl = url + "/upload" + metadata + key;

    System.out.println("Your S3 pre-signed url is ready: " + uploadurl);
    return uploadurl;
  }

  protected String getSignature(String data, String secretKey) {
    String key = secretKey + Date.class.toString();
    return key;
  }

  public boolean uploadFile(String key, byte[] data) {
    System.out.println("Uploaded your file " + data + " with key " + key + " to S3 bucket.");
    return true;
  }

  public byte[] downloadFile(String key) {
    System.out.println("S3 file Download success!");
    byte[] data = "Downloaded file contents".getBytes();
    return data;
  }
}

class GoogleCloudObjectStorage extends FileStorageService {
  private final String url = "https://api.googlecloud.com/files";
  private final String secretKey = "aenansh-mittal";

  public String getPresignedUrl(String metadata) {
    String key = getSignature(metadata, secretKey);
    String uploadurl = url + "/upload" + metadata + key;

    System.out.println("Your GCOS pre-signed url is ready: " + uploadurl);
    return uploadurl;
  }

  protected String getSignature(String data, String secretKey) {
    String key = secretKey + Date.class.toString();
    return key;
  }

  public boolean uploadFile(String key, byte[] data) {
    System.out.println("Uploaded your file " + data + " with key " + key + " to GCOS.");
    return true;
  }

  public byte[] downloadFile(String key) {
    System.out.println("GCOS file Download success!");
    byte[] data = "Downloaded file contents".getBytes();
    return data;
  }
}

public class FileStorageAbstraction {
  public static void main(String[] args) {
    FileStorageService s1 = new S3Bucket();
    String url = s1.getPresignedUrl("{name: Aenansh, type: txt, size: 256}");
    byte[] data = "This is sample file data.".getBytes();
    s1.uploadFile(url, data);
    byte[] down = s1.downloadFile(url);

    System.out.println("Your file from amazon: " + down);

    FileStorageService s2 = new GoogleCloudObjectStorage();
    String signed = s2.getPresignedUrl("{name: Aenansh, type: txt, size: 256}");
    s2.uploadFile(signed, data);
    byte[] gdown = s2.downloadFile(signed);
    System.out.println("Your file from google: " + gdown);
  }
}
