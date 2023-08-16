CREATE TABLE push_subscription (
    push_subscription_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    active BOOL NOT NULL DEFAULT true,
    truck_id BIGINT(20) NOT NULL,
    PRIMARY KEY (push_subscription_id),
    FOREIGN KEY (truck_id) REFERENCES truck (truck_id)
);
