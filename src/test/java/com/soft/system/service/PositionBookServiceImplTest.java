package com.soft.system.service;

import com.soft.system.model.PositionBook;
import com.soft.system.repo.PositionBookRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.soft.system.utils.TestData.BUY_POSITION_ACC1_SEC1;
import static com.soft.system.utils.TestData.BUY_POSITION_ACC2_SECXYZ;
import static com.soft.system.utils.TestData.BUY_SELL_CANCELLED_POSITION_ACC1_SEC1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionBookServiceImplTest {
    @InjectMocks
    private PositionBookServiceImpl positionBookService;
    @Mock
    private PositionBookRepo positionBookRepo;

    @Test
    void testGetAllPositions() {
        Set<PositionBook> positionBooks = Set.of(BUY_POSITION_ACC1_SEC1, BUY_SELL_CANCELLED_POSITION_ACC1_SEC1, BUY_POSITION_ACC2_SECXYZ);
        when(positionBookRepo.getPositions()).thenReturn(positionBooks);
        Set<PositionBook> retrievedPositionBookSet = positionBookService.getPositions();
        assertEquals(positionBooks, retrievedPositionBookSet);
    }

    @Test
    void testGetFilteredPositionsByAccountAndSecurityInformation() {
        String account = "ACC1";
        String security = "SEC1";
        when(positionBookRepo.getPositions()).thenReturn(Set.of(BUY_POSITION_ACC1_SEC1, BUY_POSITION_ACC2_SECXYZ, BUY_SELL_CANCELLED_POSITION_ACC1_SEC1));
        Set<PositionBook> positionBooks = positionBookService.getPositions(account, security);
        assertEquals(Set.of(BUY_POSITION_ACC1_SEC1, BUY_SELL_CANCELLED_POSITION_ACC1_SEC1), positionBooks);
    }
}