-- add event_id to truck
ALTER TABLE truck ADD event_id BIGINT(20);

UPDATE truck t
    SET t.event_id = (SELECT p.event_id FROM participation p WHERE t.truck_id = p.truck_id);

ALTER TABLE truck ADD FOREIGN KEY(event_id) REFERENCES event(event_id);

-- add truck_id to menu, order_info
ALTER TABLE menu ADD truck_id BIGINT(20);
ALTER TABLE order_info ADD truck_id BIGINT(20);

UPDATE menu m
    SET m.truck_id = (SELECT p.truck_id FROM participation p WHERE m.participation_id = p.participation_id);

UPDATE order_info o
    SET o.truck_id = (SELECT p.truck_id FROM participation p WHERE o.participation_id = p.participation_id);

-- drop participation
ALTER TABLE menu DROP FOREIGN KEY menu_ibfk_1;
ALTER TABLE menu DROP participation_id;
ALTER TABLE order_info DROP FOREIGN KEY order_info_ibfk_1;
ALTER TABLE order_info DROP participation_id;

DROP TABLE participation;
