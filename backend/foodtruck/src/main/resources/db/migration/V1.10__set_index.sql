CREATE INDEX ix_account_username ON account (username);
CREATE INDEX ix_account_phone ON account (phone);
CREATE INDEX ix_pushSubscription_truckId_token ON push_subscription (truck_id, token);
CREATE INDEX ix_orderInfo_truckId_status_createdAt_desc ON order_info (truck_id ASC, status ASC, created_at DESC);
