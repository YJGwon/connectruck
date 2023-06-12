CREATE TABLE truck (
    truck_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    close_hour TIME NOT NULL,
    open_hour TIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255),
    PRIMARY KEY (truck_id)
);
