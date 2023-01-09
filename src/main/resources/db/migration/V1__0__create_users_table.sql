create table users
(
    id BIGINT NOT NULL,
    email VARCHAR (100),
    token VARCHAR (100),
    user_name VARCHAR(100),
    bio VARCHAR (100),
    image VARCHAR (100),
    password VARCHAR (100),
    PRIMARY KEY(id)
)