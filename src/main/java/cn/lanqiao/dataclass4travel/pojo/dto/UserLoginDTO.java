package cn.lanqiao.dataclass4travel.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private String userName;
    private String passWord;
    private String userCode;
}
