CREATE TABLE event (
    event_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id)
);

CREATE TABLE schedule (
    schedule_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    event_date DATE NOT NULL,
    open_hour TIME NOT NULL,
    close_hour TIME NOT NULL,
    event_id BIGINT(20) NOT NULL,
    PRIMARY KEY (schedule_id),
    FOREIGN KEY (event_id) REFERENCES event (event_id)
);

CREATE TABLE participation (
    participation_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    event_id BIGINT(20) NOT NULL,
    truck_id BIGINT(20) NOT NULL,
    PRIMARY KEY (participation_id),
    FOREIGN KEY (event_id) REFERENCES event (event_id),
    FOREIGN KEY (truck_id) REFERENCES truck (truck_id)
);

ALTER TABLE truck DROP open_hour;
ALTER TABLE truck DROP close_hour;
ALTER TABLE truck DROP location;
ALTER TABLE truck ADD car_number VARCHAR(8) NOT NULL;
