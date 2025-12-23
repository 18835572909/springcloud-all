package com.hz.voa.pojo;

import lombok.Data;

/**
 * 
 * @author rhb
 * @date 2025/12/23 18:33
 **/
@Data
public class OrderRequest {
    private String userId;
    private String commodityCode;
    private int orderCount;
}
