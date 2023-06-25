package com.connectruck.foodtruck.common.testbase;

import com.connectruck.foodtruck.common.fixture.DataSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class ServiceTestBase {

    @Autowired
    protected DataSetup dataSetup;
}
