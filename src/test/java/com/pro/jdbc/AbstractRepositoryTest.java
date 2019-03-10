package com.pro.jdbc;

import com.pro.entity.Tester;
import com.pro.resource.Category;
import com.pro.resource.annotations.Table;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataTestConfiguration.class)
public class AbstractRepositoryTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate template;

    @Before
    public void setUp(){
        template = new JdbcTemplate(dataSource);
        template.update("DELETE FROM " + Tester.class.getAnnotation(Table.class).name() + " WHERE 1");
    }

    @Test
    public void save(){
        AbstractRepository<Tester> repository = new AbstractRepository<Tester>(dataSource, Tester.class) {};
        String name = "testname";
        Tester tester = new Tester();
        tester.setCategory(Category.FIRST);
        tester.setLala(Category.SECOND);
        tester.setName(name);
        repository.save(tester);

        Tester testerFromDb = template.queryForObject("SELECT * FROM " + Tester.class.getAnnotation(Table.class).name() + " WHERE dwadwa = '" + name + "'", repository.mapper());
        tester.setId(testerFromDb.getId());

        assert tester.equals(testerFromDb);

    }

}