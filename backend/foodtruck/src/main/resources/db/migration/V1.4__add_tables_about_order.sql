CREATE TABLE order_info (
    order_info_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    participation_id BIGINT(20) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (order_info_id),
    FOREIGN KEY (participation_id) REFERENCES participation (participation_id)
);

CREATE TABLE order_line (
    order_line_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_info_id BIGINT(20) NOT NULL,
    menu_id BIGINT(20) NOT NULL,
    menu_name VARCHAR(255) NOT NULL,
    menu_price DECIMAL NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_line_id),
    FOREIGN KEY (order_info_id) REFERENCES order_info (order_info_id)
);
