package com.example.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "names")
    private String lastNames;
    @Column(nullable = false, name = "last_names")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;
}
