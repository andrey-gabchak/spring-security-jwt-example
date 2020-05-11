package com.gabchak.example.models;

import com.gabchak.example.dto.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "roles",
    uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "name")
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Collection<User> users;

  /**
   * Build a new role from role enum.
   *
   * @param roleEnum {@link Roles}
   * @return a new {@link Role}
   */
  public static Role fromEnum(Roles roleEnum) {
    Role role = new Role();
    role.setId(roleEnum.getId());
    role.setName(roleEnum.name());
    return role;
  }
}
