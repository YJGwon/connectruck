-- 사장님 계정 (비밀번호는 test1234!)
INSERT INTO account
    (username, password, phone, role)
VALUES
    ('owner1', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000001', 'OWNER'),
    ('owner2', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000002', 'OWNER'),
    ('owner3', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000003', 'OWNER'),
    ('owner4', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000004', 'OWNER'),
    ('owner5', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000005', 'OWNER'),
    ('owner6', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000006', 'OWNER'),
    ('owner7', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000007', 'OWNER'),
    ('owner8', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000008', 'OWNER'),
    ('owner9', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000009', 'OWNER'),
    ('owner10', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000010', 'OWNER'),
    ('owner11', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000011', 'OWNER'),
    ('owner12', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000012', 'OWNER'),
    ('owner13', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000013', 'OWNER'),
    ('owner14', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000014', 'OWNER'),
    ('owner15', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000015', 'OWNER'),
    ('owner16', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000016', 'OWNER'),
    ('owner17', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000017', 'OWNER'),
    ('owner18', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000018', 'OWNER'),
    ('owner19', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000019', 'OWNER'),
    ('owner20', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000020', 'OWNER'),
    ('owner21', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000021', 'OWNER'),
    ('owner22', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000022', 'OWNER'),
    ('owner23', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000023', 'OWNER'),
    ('owner24', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000024', 'OWNER'),
    ('owner25', 'b567745a4550cd651791a6b20b9b3bf7985738607599c38782218788d79acba1', '01000000025', 'OWNER')
;

UPDATE truck
    SET owner_id = truck_id;
