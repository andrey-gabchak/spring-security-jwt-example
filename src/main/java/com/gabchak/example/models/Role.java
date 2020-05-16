package com.gabchak.example.models;

import com.gabchak.example.dto.enums.Roles;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

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
