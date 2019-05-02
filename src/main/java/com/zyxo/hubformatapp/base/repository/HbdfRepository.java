package com.zyxo.hubformatapp.base.repository;

import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HbdfRepository extends JpaRepository<HbdfEntity, Integer> {


    List<HbdfEntity> findAll();

    Optional<HbdfEntity> findById(String id);

    Optional<HbdfEntity> findByName(String name);
}
