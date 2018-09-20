package com.pro.jdbc;

import com.pro.entity.Who;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Service
public class WhoDAO extends AbstractRepository<Who>{

    public WhoDAO(DataSource dataSource) {
        super(dataSource, Who.class);
    }

    public Optional<Who> findById(int id) throws ClassNotFoundException, IllegalAccessException {
        Who who = new Who();
        who.setId(id);
        return super.findByParameters(who);
    }

    public Optional<Who> findByIdAndName(int id, String name) throws ClassNotFoundException, IllegalAccessException {
        Who who = new Who();
        who.setId(id);
        who.setName(name);
        return super.findByParameters(who);
    }
}
