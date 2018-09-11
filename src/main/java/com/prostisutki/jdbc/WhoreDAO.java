package com.prostisutki.jdbc;

import com.prostisutki.entity.Whore;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Service
public class WhoreDAO extends AbstractRepository<Whore>{

    public WhoreDAO(DataSource dataSource) {
        super(dataSource, Whore.class);
    }

    public Optional<Whore> findById(int id) throws ClassNotFoundException, IllegalAccessException {
        Whore whore = new Whore();
        whore.setId(id);
        return super.findByParameters(whore);
    }

    public Optional<Whore> findByIdAndName(int id, String name) throws ClassNotFoundException, IllegalAccessException {
        Whore whore = new Whore();
        whore.setId(id);
        whore.setName(name);
        return super.findByParameters(whore);
    }
}
