package com.jpmc.ib.service;

import com.jpmc.ib.model.PositionBook;

import java.util.Set;

public interface PositionBookService {
    Set<PositionBook> getPositions();

    Set<PositionBook> getPositions(String account, String security);
}
