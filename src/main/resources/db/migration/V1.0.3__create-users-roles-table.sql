CREATE TABLE IF NOT EXISTS users_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY(user_id, role_id),
  FOREIGN KEY (user_id)
    REFERENCES registered_users(id)
      ON DELETE CASCADE,
  FOREIGN KEY (role_id)
    REFERENCES roles(id)
      ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
