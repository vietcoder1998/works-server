package com.worksvn.student_service.utils.base;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import com.google.firebase.cloud.StorageClient;

public class FileUploadUtils {

    public static String uploadFile(String dir, String fileName,
                                    byte[] data,
                                    String contentType) throws StorageException {
        Blob avatarFile = StorageClient.getInstance()
                .bucket()
                .create(dir + "/" + fileName, data, contentType);
        return FileUploadUtils.getDownloadUrl(avatarFile.getBucket(), avatarFile.getName());
    }

    public static String getDownloadUrl(String bucketUrl, String fileName) {
        return "http://storage.googleapis.com/" + bucketUrl + "/" + fileName;
    }
}
