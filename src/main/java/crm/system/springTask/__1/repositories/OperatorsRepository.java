package crm.system.springTask.__1.repositories;


import crm.system.springTask.__1.db.Operators;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface OperatorsRepository extends JpaRepository<Operators, Long> {
}
