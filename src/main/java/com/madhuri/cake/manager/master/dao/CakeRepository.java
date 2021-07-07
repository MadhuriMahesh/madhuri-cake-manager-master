package com.madhuri.cake.manager.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CakeRepository  extends JpaRepository<CakeEntity, Long> {

    List<CakeEntity> findAllByUserId(String id);
}
