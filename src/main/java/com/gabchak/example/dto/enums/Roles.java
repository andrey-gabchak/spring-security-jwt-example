package com.gabchak.example.dto.enums;

import lombok.Getter;

@Getter
public enum Roles {
  ADMIN(1),
  FREE_USER(2),
  PAID_USER(3);

  private final Integer id;

  Roles(Integer id) {
    this.id = id;
  }
}
