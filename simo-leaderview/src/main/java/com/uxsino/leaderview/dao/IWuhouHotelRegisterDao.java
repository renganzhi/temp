package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.WuhouHotelRegister;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: hxd
 * @Date: 2022/3/5 18:09
 * @Description:
 * @Version: 1.0
 */
public interface IWuhouHotelRegisterDao extends ICustomRepository<WuhouHotelRegister, Long> {
    @Query(value = "select guest_name,count(guest_identity) from wuhou_hotel_register where guest_identity is not null and is_deleted = 0 group by guest_identity,guest_name order by count(*) desc LIMIT ?1",nativeQuery = true)
    List<String[]> getHousingPersonnelTop(Integer top);

    @Query(value = "select guest_name,count(distinct (hotel_id)),guest_identity from wuhou_hotel_register where guest_identity is not null and is_deleted = 0 group by guest_identity,guest_name order by count(distinct(hotel_id)) desc LIMIT ?1",nativeQuery = true)
    List<String[]> getFlowHousingPersonnelTop(Integer top);
}
