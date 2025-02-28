package crm.system.springTask.__1.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class ApplicationRequest {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column
    private Long id;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, length = 1000)
    private String commentary;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private boolean handled; //обработано или нет

    @ManyToOne Courses courses;

    @ManyToMany
    @JoinTable(
            name = "request_operators",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "operator_id")
    )
    private List<Operators> operators;
}