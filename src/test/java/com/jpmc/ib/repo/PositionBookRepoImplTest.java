package com.jpmc.ib.repo;

import com.jpmc.ib.model.PositionBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.jpmc.ib.utils.TestData.*;
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
        Set<PositionBook> positionBooksForSave = Set.of(BUY_POSITION_ACC1_SEC1, BUY_SELL_CANCELLED_POSITION_ACC1_SEC1, BUY_POSITION_ACC2_SECXYZ);
        positionBookRepo.save(positionBooksForSave);
        Set<PositionBook> positionBooks = positionBookRepo.getPositions();
        assertEquals(positionBooksForSave, positionBooks);
    }
}