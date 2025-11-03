package org.city.alert.alert.service.repository;

import org.city.alert.alert.service.dto.*;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.enums.Department;
import org.city.alert.alert.service.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByWorkerId(Long workerId);

    List<Issue> findByUserId(Long userId);

    // Admin issue report
//    @Query("""
//        SELECT new org.city.alert.alert.service.dto.AdminIssueReportDTO(
//            i.id,
//            i.title,
//            i.status,
//            i.category,
//            u.department,
//            CONCAT(u.name, ' ', u.surname),
//            i.priority,
//            SIZE(i.statusHistory),
//            CASE
//                WHEN EXISTS (SELECT sh FROM i.statusHistory sh WHERE sh.status = org.city.alert.alert.service.enums.Status.RESOLVED)
//                THEN (
//                    SELECT EXTRACT(EPOCH FROM (sh.updatedAt - i.reportedAt))
//                    FROM i.statusHistory sh
//                    WHERE sh.status = org.city.alert.alert.service.enums.Status.RESOLVED
//                    ORDER BY sh.updatedAt ASC
//                    LIMIT 1
//                )
//                ELSE 0
//            END
//        )
//        FROM Issue i
//        LEFT JOIN org.city.alert.alert.service.entity.User u ON i.workerId = u.id
//        WHERE i.isDeleted = false
//    """)
//    List<AdminIssueReportDTO> fetchAdminReport();

    // Count by status
    @Query("""
        SELECT new org.city.alert.alert.service.dto.StatusCountDTO(i.status, COUNT(i))
        FROM Issue i
        WHERE i.isDeleted = false
        GROUP BY i.status
    """)
    List<StatusCountDTO> countByStatus();

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.isDeleted = false")
    long countAllIssues();

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = 'OPEN' AND i.isDeleted = false")
    long countOpenIssues();

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = 'IN_PROGRESS' AND i.isDeleted = false")
    long countInProgressIssues();

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = 'RESOLVED' AND i.isDeleted = false")
    long countResolvedIssues();

    @Query(value = """
    SELECT AVG(EXTRACT(EPOCH FROM (sh.updated_at - i.reported_at)))
    FROM issues i
    JOIN issue_status_history sh ON sh.issue_id = i.id
    WHERE sh.status = 'RESOLVED'
    """, nativeQuery = true)
    Double averageResolutionTimeSeconds();

    @Query("""
           SELECT i.workerId, COUNT(i)
           FROM Issue i
           WHERE i.workerId IS NOT NULL
           GROUP BY i.workerId
           """)
    List<Object[]> countIssuesByTechnician();

    long countByWorkerId(Long id);

    long countByWorkerIdAndStatus(Long id, Status status);

    @Query("""
    SELECT u.department,
           COUNT(i.id) AS total,
           SUM(CASE WHEN i.status = 'OPEN' THEN 1 ELSE 0 END) AS openCount,
           SUM(CASE WHEN i.status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS inProgressCount,
           SUM(CASE WHEN i.status = 'RESOLVED' THEN 1 ELSE 0 END) AS resolvedCount
    FROM Issue i
    LEFT JOIN User u ON i.workerId = u.id
    GROUP BY u.department
""")
    List<Object[]> countIssuesByDepartment();
    int countByWorkerIdAndStatusNot(Long workerId, String status);

    long countByWorkerIdAndStatusIn(Long workerId, List<Status> open);


    // Count by department
//    @Query("""
//        SELECT new org.city.alert.alert.service.dto.DepartmentCountDTO(u.department, COUNT(i))
//        FROM Issue i
//        JOIN org.city.alert.alert.service.entity.User u ON i.workerId = u.id
//        WHERE i.isDeleted = false
//        GROUP BY u.department
//    """)
//    List<DepartmentCountDTO> countByDepartment();

    // Technician workload
//    @Query("""
//        SELECT new org.city.alert.alert.service.dto.TechnicianWorkloadDTO(
//            CONCAT(u.name, ' ', u.surname),
//            u.department,
//            COUNT(i),
//            SUM(CASE WHEN i.status = org.city.alert.alert.service.enums.Status.RESOLVED THEN 1 ELSE 0 END)
//        )
//        FROM org.city.alert.alert.service.entity.User u
//        LEFT JOIN Issue i ON i.workerId = u.id
//        WHERE u.role = org.city.alert.alert.service.enums.UserRoles.TECHNICIAN
//        GROUP BY u.id, u.department
//    """)
//    List<TechnicianWorkloadDTO> fetchTechnicianWorkload();

    // Average resolution time
//    @Query("""
//        SELECT AVG(EXTRACT(EPOCH FROM (sh.updatedAt - i.reportedAt)))
//        FROM Issue i
//        JOIN i.statusHistory sh
//        WHERE sh.status = org.city.alert.alert.service.enums.Status.RESOLVED
//    """)
//    Double averageResolutionTimeSeconds();
}
