package org.loong.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagUpdateVo {

    private Long id;
    private String name;
    private String remark;
}
