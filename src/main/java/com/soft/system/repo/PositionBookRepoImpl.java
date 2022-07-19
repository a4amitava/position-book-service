package com.soft.system.repo;

import com.soft.system.model.PositionBook;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Repository
public class PositionBookRepoImpl implements PositionBookRepo {

    private Set<PositionBook> positionBooks = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void save(final Set<PositionBook> positionBooks) {
        this.positionBooks = positionBooks;
    }

    @Override
    public Set<PositionBook> getPositions() {
        return positionBooks;
    }
}
