package com.jpmc.ib;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor
class PositionBookEventProcessingAppTests {

    private final ApplicationContext applicationContext;

    /** This test case is to ensure application will start successfully, i,e can load all bean.*/
    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }
}
