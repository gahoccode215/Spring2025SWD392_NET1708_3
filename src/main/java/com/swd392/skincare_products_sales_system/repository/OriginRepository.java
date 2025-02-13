package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Brand;
import com.swd392.skincare_products_sales_system.model.Origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OriginRepository extends JpaRepository<Origin, Long> {
    Optional<Origin> findByIdAndIsDeletedFalse(Long originId);

    @Query("SELECT x FROM Origin x WHERE x.slug = :slug AND x.isDeleted = false AND x.status = com.swd392.skincare_products_sales_system.enums.Status.ACTIVE")
    Optional<Origin> findBySlugAndStatusAndIsDeletedFalse(@Param("slug") String slug);

}
