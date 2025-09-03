package org.city.alert.alert.service.repository;

import org.city.alert.alert.service.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Optional<List<Issue>> findByWorkerId(Long id);
}
