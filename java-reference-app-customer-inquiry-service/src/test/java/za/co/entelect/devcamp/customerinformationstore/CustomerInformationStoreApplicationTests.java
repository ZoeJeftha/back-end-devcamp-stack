package za.co.entelect.devcamp.customerinformationstore;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class CustomerInformationStoreApplicationTests {

//    @ClassRule
//    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.2")
//            .withDatabaseName("postgres")
//            .withUsername("user")
//            .withPassword("password");

    @Test
    void contextLoads() {
    }

}
