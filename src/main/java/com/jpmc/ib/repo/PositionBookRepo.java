package com.jpmc.ib.repo;

import com.jpmc.ib.model.PositionBook;

import java.util.Set;

public interface PositionBookRepo {

    void save(final Set<PositionBook> positionBooks);

    Set<PositionBook> getPositions();
}
