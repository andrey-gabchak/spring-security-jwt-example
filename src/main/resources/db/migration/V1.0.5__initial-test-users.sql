REPLACE INTO registered_users
(
    id,
    email,
    first_name,
    last_name,
    password
)
VALUES
(
    1,
    'admin@gmail.com',
    'admin',
    'adminovich',
    '$2a$10$FXGLHqz2PmsEmviyadsFYenyutHsqNUrISCYnkfq6QdXTjjrpLcLy'
);

REPLACE INTO users_roles (user_id, role_id)
VALUES (1, 1);

REPLACE INTO users_roles (user_id, role_id)
VALUES (1, 2);

REPLACE INTO users_roles (user_id, role_id)
VALUES (1, 3);



REPLACE INTO registered_users
(
    id,
    email,
    first_name,
    last_name,
    password
)
VALUES
(
    2,
    'free@gmail.com',
    'free',
    'freevovich',
    '$2a$10$Ol.hy4t35m/S7Y1sjdb4fuvmvAH56ugFLc84WaYSEf0V3bMVYzy4i'
);

REPLACE INTO users_roles (user_id, role_id)
VALUES (2, 2);


REPLACE INTO registered_users
(
    id,
    email,
    first_name,
    last_name,
    password
)
VALUES
(
    3,
    'paid@gmail.com',
    'paid',
    'paidovich',
    '$2a$10$C6bTTNW7MZhWQe2mqE9f3e2hmiI/FKGU0zRl8H0QA81oeOALlovPe'
);

REPLACE INTO users_roles (user_id, role_id)
VALUES (2, 2);

REPLACE INTO users_roles (user_id, role_id)
VALUES (2, 3);
