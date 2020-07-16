package com.owesome.facerecognition.mapper;

import com.owesome.facerecognition.entity.FaceFeatureRepoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
//Idea的Bug：Idea不把Mapper注解的类当成Spring Bean
@Component
public interface FaceRecognitionMapper {

    @Select("<script>"
            + "select id,name,image from face_feature_repo where 1=1 "
            + "<if test='face.id!=null and face.id!=\"\"'> and id=#{face.id}</if>"
            + "<if test='face.name!=null and face.name!=\"\"'> and name=#{face.name}</if>"
            + " order by name asc"
            + " limit #{pageSize} offset #{pageOffset} "
            + "</script>")
    List<FaceFeatureRepoEntity> queryFaceListByCondition(@Param("face") FaceFeatureRepoEntity face,
                                                     @Param("pageSize") Integer pageSize,
                                                     @Param("pageOffset") Integer pageOffset);


    @Select("<script>"
            + "select count(1) from face_feature_repo where 1=1 "
            + "<if test='face.id!=null and face.id!=\"\"'> and id=#{face.id}</if>"
            + "<if test='face.name!=null and face.name!=\"\"'> and name=#{face.name}</if>"
            + "</script>")
    Integer queryFaceCountByCondition(@Param("face") FaceFeatureRepoEntity face);
}
