// IFileService.aidl
package com.ke.hs;

// Declare any non-default types here with import statements

interface IFileService {
    List<String> getFiles(String path);
    void createFile(String path, String name);

    boolean deleteFile(String path);

    void copyFile(String origin, String target);


    long lastModified(String path);

    void moveFile(String origin, String target);

    long fileSize(String path);

    void clearFile(String path);
    boolean copyAndClearFile(String origin, String target);
}