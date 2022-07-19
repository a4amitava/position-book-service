package com.soft.system.service;

import com.soft.system.model.PositionBook;
import com.soft.system.repo.PositionBookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionBookServiceImpl implements PositionBookService {

    private final PositionBookRepo positionBookRepo;

    @Override
    public Set<PositionBook> getPositions() {
        return positionBookRepo.getPositions();
    }

    @Override
    public Set<PositionBook> getPositions(String account, String security) {
        Set<PositionBook> positionBooks = positionBookRepo.getPositions();
        return positionBooks.stream()
                .filter(positionBook -> positionBook.getAccount().equals(account) && positionBook.getSecurity().equals(security))
                .collect(Collectors.toSet());
    }
}
