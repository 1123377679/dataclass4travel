package cn.lanqiao.dataclass4travel.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KindEditorResult {

    private int error;
    private String url;
    private String message;
}
