package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    @Query("SELECT f FROM Feature f WHERE f.id IN :featureIds AND f.isDeleted = false")
    Set<Feature> findAllByIdAndIsDeletedFalse(@Param("featureIds") Set<Long> featureIds);
}
