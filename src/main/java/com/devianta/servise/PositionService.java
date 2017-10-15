package com.devianta.servise;

import com.devianta.model.Department;
import com.devianta.model.Position;
import com.devianta.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    PositionRepository positionRepository;

    @Transactional(readOnly = true)
    public List<Position> findByDepartment(Department department) {
        return positionRepository.findByDepartment(department);
    }

    @Transactional
    public void save(Position position) {
        positionRepository.save(position.normalise());
    }
}
