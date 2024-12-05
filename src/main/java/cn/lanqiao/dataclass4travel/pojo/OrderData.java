package cn.lanqiao.dataclass4travel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderData {


    private  int value;

    private  String name;

    private  int type;


}
