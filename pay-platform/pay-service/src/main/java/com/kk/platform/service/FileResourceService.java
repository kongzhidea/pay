package com.kk.platform.service;

import com.kk.platform.dao.FileResourceDao;
import com.kk.platform.model.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileResourceService {
    @Autowired
    private FileResourceDao fileResourceDao;

    public void createFileResource(FileResource fileResource) {
        if (fileResource == null) {
            throw new IllegalArgumentException();
        }

        fileResourceDao.insert(fileResource);
    }

    public FileResource getFileResource(int id) {
        return fileResourceDao.selectById(id);
    }

    public FileResource getFileResource(String fileId) {
        return fileResourceDao.selectByFileId(fileId);
    }

}
