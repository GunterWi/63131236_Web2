package com.ngt.cuoiky.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;


    public long countOrderByWeek(){
        return reportRepository.countOrderByWeek();
    }

    public long countUserByWeek(){
        return reportRepository.countUserByWeek();
    }

    public long getTotalOrderByWeek(){
        return reportRepository.totalOrderByWeek();
    }

}
