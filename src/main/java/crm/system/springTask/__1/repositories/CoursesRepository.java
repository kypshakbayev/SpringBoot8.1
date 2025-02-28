package crm.system.springTask.__1.repositories;


import crm.system.springTask.__1.db.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CoursesRepository extends JpaRepository<Courses, Long> {
}
