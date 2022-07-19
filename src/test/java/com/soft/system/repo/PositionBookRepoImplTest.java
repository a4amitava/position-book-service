package com.soft.system.repo;

import com.soft.system.model.PositionBook;
import com.soft.system.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PositionBookRepoImplTest {
    @InjectMocks
    private PositionBookRepoImpl positionBookRepo;

    @Test
    void tesSavedPositionRetrieval() {
        // when there is no position stored then check empty
        assertTrue(positionBookRepo.getPositions().isEmpty());
        // next - save the positions before retrieval
        Set<PositionBook> positionBooksForSave = Set.of(TestData.BUY_POSITION_ACC1_SEC1, TestData.BUY_SELL_CANCELLED_POSITION_ACC1_SEC1, TestData.BUY_POSITION_ACC2_SECXYZ);
        positionBookRepo.save(positionBooksForSave);
        Set<PositionBook> positionBooks = positionBookRepo.getPositions();
        assertEquals(positionBooksForSave, positionBooks);
    }
}