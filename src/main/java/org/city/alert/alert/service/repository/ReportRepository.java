package org.city.alert.alert.service.repository;

import org.city.alert.alert.service.dto.*;
import org.city.alert.alert.service.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Issue, Long> {


    // 5️⃣ Citizen issues (not deleted)
    List<Issue> findByUserIdAndIsDeletedFalse(Long userId);

    // 6️⃣ Technician issues (not deleted)
    List<Issue> findByWorkerIdAndIsDeletedFalse(Long workerId);

}
