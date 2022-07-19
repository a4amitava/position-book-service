package com.soft.system.repo;

import com.soft.system.model.PositionBook;

import java.util.Set;

public interface PositionBookRepo {

    void save(final Set<PositionBook> positionBooks);

    Set<PositionBook> getPositions();
}
