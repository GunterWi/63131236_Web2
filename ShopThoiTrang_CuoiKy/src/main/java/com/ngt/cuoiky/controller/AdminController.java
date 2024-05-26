package com.ngt.cuoiky.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ngt.cuoiky.service.ReportService;

@Controller
public class AdminController {

    @Autowired
    private ReportService reportService;
    
    @GetMapping("/admin")
    public String adminDashboard(Model model) throws ParseException {
        long countUserByWeek = reportService.countUserByWeek();
        long countOrderByWeek = reportService.countOrderByWeek();
        long totalOrderByWeek = reportService.getTotalOrderByWeek();

        model.addAttribute("countUserByWeek", countUserByWeek);
        model.addAttribute("countOrderByWeek", countOrderByWeek);
        model.addAttribute("totalOrderByWeek", totalOrderByWeek);

        return "admin";
    }

}
