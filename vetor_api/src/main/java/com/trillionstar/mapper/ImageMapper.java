package com.trillionstar.mapper;

import com.trillionstar.entity.Image;

import java.util.List;

public interface ImageMapper {
    int insertImage(Image image);
    List<String> queryImagePath(String i_imageurl);
    int delImage(String i_imageurl);
}
