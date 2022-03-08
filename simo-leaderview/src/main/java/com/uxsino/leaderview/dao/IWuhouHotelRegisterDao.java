package com.uxsino.leaderview.dao;

import com.uxsino.commons.db.repository.ICustomRepository;
import com.uxsino.leaderview.entity.WuhouHotelRegister;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IWuhouHotelRegisterDao extends ICustomRepository<WuhouHotelRegister, Long> {
    @Query(value = "select guest_name,count(guest_identity) from wuhou_hotel_register where guest_identity is not null and is_deleted = 0 group by guest_identity,guest_name order by count(*) desc LIMIT ?1",nativeQuery = true)
    List<String[]> getHousingPersonnelTop(Integer top);

    @Query(value = "select guest_name,count(distinct (hotel_id)),guest_identity from wuhou_hotel_register where guest_identity is not null and is_deleted = 0 group by guest_identity,guest_name order by count(distinct(hotel_id)) desc LIMIT ?1",nativeQuery = true)
    List<String[]> getFlowHousingPersonnelTop(Integer top);

    @Query( "from WuhouHotelRegister w where w.isDeleted = 0 and w.checkInDate >= ?1 and w.checkInDate < ?2 ")
    List<WuhouHotelRegister> findBycheckInDate(Date startTime, Date endTime);

    @Query( "from WuhouHotelRegister w where w.isDeleted = 0 and w.checkOutDate >= ?1 and w.checkOutDate < ?2")
    List<WuhouHotelRegister> findByCheckOutDateDate(Date startTime, Date endTime);

    @Query(value = "select w.hotelId,count(w.hotelId) from WuhouHotelRegister w where w.isDeleted = 0 and w" +
            ".createTime >=?1 " +
            "group by w.hotelId order by count(w.hotelId) desc")
    List<String[]> getLiveSortDesc(Date startTime);

    @Query(value = "select w.hotelId,count(w.hotelId) from WuhouHotelRegister w where w.isDeleted = 0 and w.createTime >?1 " +
            "group by w.hotelId order by count(w.hotelId) asc")
    List<String[]> getLiveSortAsc(Date startTime);

    List<WuhouHotelRegister> findByHotelId(Long hotelId);
}
