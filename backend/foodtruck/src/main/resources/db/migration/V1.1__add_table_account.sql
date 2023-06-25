CREATE TABLE account (
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    role VARCHAR(50) NOT NULL,
    username VARCHAR(255),
    primary key (user_id)
)
