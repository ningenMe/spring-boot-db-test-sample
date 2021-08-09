package ningenme.net.sample.mapper.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
  private Integer id;
  private String name;
  private LocalDate deletedTime;
  private LocalDate createdTime;
  private LocalDate updatedTime;
}
