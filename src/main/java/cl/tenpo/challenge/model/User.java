package cl.tenpo.challenge.model;

import lombok.*;

import javax.persistence.*;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Entity
@Table(name = "users_challenge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean hasValidToken;
}
