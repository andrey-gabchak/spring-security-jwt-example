package com.gabchak.example.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "registered_users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @org.springframework.lang.NonNull
  @Column(name = "email")
  private String email;
  @NonNull
  @Column(name = "password")
  private String password;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "subscription")
  private LocalDate subscription;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
  private Collection<Role> roles;

}
