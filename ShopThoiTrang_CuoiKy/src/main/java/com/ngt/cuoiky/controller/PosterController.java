package com.ngt.cuoiky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ngt.cuoiky.model.Poster;
import com.ngt.cuoiky.service.PosterService;
import com.ngt.cuoiky.service.StorageService;

@Controller
public class PosterController {
    @Autowired
    private PosterService posterService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/admin/poster")
    public String listPoster(Model model) {
        List<Poster> listPostersLeft = posterService.listPosterLeft();
        List<Poster> listPostersRight = posterService.listPosterRight();
        model.addAttribute("listPostersLeft",listPostersLeft);
        model.addAttribute("listPostersRight",listPostersRight);
        return "admin/poster";
    }
}
