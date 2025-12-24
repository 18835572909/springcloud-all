package com.hz.voa.transform;

import com.hz.voa.entity.Order;
import com.hz.voa.pojo.OrderVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 
 * @author rhb
 * @date 2025/12/23 18:45
 **/
@Mapper
//        (componentModel = "spring",
//        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
//        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
//)
public interface OrderMapping {

    OrderMapping INSTANCE = Mappers.getMapper(OrderMapping.class);

    OrderVO toVo(Order order);
}
