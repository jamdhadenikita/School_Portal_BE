package com.sc.repository;



import com.sc.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {
    Optional<LeaveTypeEntity> findByLeaveCode(String leaveCode);
}