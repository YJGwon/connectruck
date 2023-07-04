CREATE TABLE menu (
    menu_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    participation_id BIGINT(20) NOT NULL,
    PRIMARY KEY (menu_id),
    FOREIGN KEY (participation_id) REFERENCES participation (participation_id)
);
