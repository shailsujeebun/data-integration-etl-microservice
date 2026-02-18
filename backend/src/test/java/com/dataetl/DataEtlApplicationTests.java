package com.dataetl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.dataetl.service.DynamicSchedulerService;

@SpringBootTest
@ActiveProfiles("test")
class DataEtlApplicationTests {

    @MockBean
    private DynamicSchedulerService dynamicSchedulerService;

    @Test
    void contextLoads() {
        // Verify Spring context loads without errors
    }
}
