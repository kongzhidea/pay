package com.kk.platform.dao;

import com.kk.platform.model.FileResource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@MyBatisRepository
public interface FileResourceDao {

    String FIELDS = " id, file_id, data, name, size, file_type ";
    String TABLE_NAME = "file_resource";

    @Insert("insert into " + TABLE_NAME + "(file_id, data, name, size, file_type) " +
            "values(#{fileId},#{data},#{name},#{size},#{fileType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(FileResource fileResource);


    @Select("select " + FIELDS + " from " + TABLE_NAME + " where id = #{id}")
    FileResource selectById(int id);

    @Select("select " + FIELDS + " from " + TABLE_NAME + " where file_id = #{fileId}")
    FileResource selectByFileId(String fileId);
}
