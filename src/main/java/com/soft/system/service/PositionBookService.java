package com.soft.system.service;

import com.soft.system.model.PositionBook;

import java.util.Set;

public interface PositionBookService {
    Set<PositionBook> getPositions();

    Set<PositionBook> getPositions(String account, String security);
}
