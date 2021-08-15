package ningenme.net.sample.mapper;

import lombok.NonNull;
import ningenme.net.sample.mapper.dto.UserDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMysqlMapper {

  @Insert(
          "<script>" +
          "INSERT INTO " +
          "     users (id, name) " +
          "VALUES " +
          "     <foreach item='user' collection='userDtoList' open='' separator=',' close=''>" +
          "     (#{user.id}, #{user.name}) " +
          "     </foreach> " +
          "</script>"
  )
  void insert(@Param("userDtoList") @NonNull final List<UserDto> userDtoList);

}
