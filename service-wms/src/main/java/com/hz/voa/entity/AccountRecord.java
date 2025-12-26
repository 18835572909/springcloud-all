package com.hz.voa.entity;

import lombok.Data;

/**
 * 
 * @author rhb
 * @date 2025/12/23 16:03
 **/
@Data
public class AccountRecord {

    private Long id;

    private String itemId;

    private Integer total;

    private String status;

    private Integer currentNum;

}
