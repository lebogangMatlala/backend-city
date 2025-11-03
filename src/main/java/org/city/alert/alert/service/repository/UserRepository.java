package org.city.alert.alert.service.repository;

import org.city.alert.alert.service.dto.TechnicianWorkloadDTO;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleAndDepartment(String technician, String category);

    boolean existsByEmail(String email);

    List<User> findByRole(String role);
    long countByRole(UserRoles role);

    @Query("""
    SELECT new org.city.alert.alert.service.dto.TechnicianWorkloadDTO(
        CONCAT(u.name, ' ', u.surname),
        u.department,
        COUNT(i),
        SUM(CASE WHEN i.status='RESOLVED' THEN 1 ELSE 0 END)
    )
    FROM User u
    LEFT JOIN Issue i ON i.workerId = u.id
    WHERE u.role = 'TECHNICIAN'
    GROUP BY u.id, u.department
""")
    List<TechnicianWorkloadDTO> fetchTechnicianWorkload();


}
