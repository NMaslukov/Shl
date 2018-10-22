package birzha;

import com.pro.configs.ShlApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = ShlApplication.class)
@RunWith(SpringRunner.class)
public class AunitWsServiceImplTest {

    @Autowired
    AunitWsServiceImpl aunitWsService;

    @Test
    public void init() {
        System.out.println("okay");
    }
}