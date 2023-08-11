-- add constrains to columns added in v1.5
ALTER TABLE menu ADD FOREIGN KEY(truck_id) REFERENCES truck(truck_id);
ALTER TABLE order_info ADD FOREIGN KEY(truck_id) REFERENCES truck(truck_id);

ALTER TABLE truck MODIFY COLUMN event_id BIGINT(20) NOT NULL;
ALTER TABLE menu MODIFY COLUMN truck_id BIGINT(20) NOT NULL;
ALTER TABLE order_info MODIFY COLUMN truck_id BIGINT(20) NOT NULL;

-- add owner
ALTER TABLE truck ADD owner_id BIGINT(20);
ALTER TABLE truck ADD FOREIGN KEY(owner_id) REFERENCES account(user_id);
