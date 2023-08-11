package com.connectruck.foodtruck.common.testbase;

import com.connectruck.foodtruck.common.config.JpaAuditingConfig;
import com.connectruck.foodtruck.common.fixture.DataSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({DataSetup.class, JpaAuditingConfig.class})
@Sql("classpath:truncate.sql")
public abstract class RepositoryTestBase {

    @Autowired
    protected DataSetup dataSetup;
}
