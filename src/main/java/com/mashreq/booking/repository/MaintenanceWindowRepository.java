package com.mashreq.booking.repository;

import com.mashreq.booking.domain.MaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindow, Long>, JpaSpecificationExecutor<MaintenanceWindow> {
}
