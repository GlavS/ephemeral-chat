package ru.otus.panel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.panel.dockerapi.DockerService;

@Controller
@Slf4j
public class AdminController {

    private final DockerService dockerService;

    public AdminController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/start")
    public ModelAndView startContainer(StartDto body) {
        log.info("startContainer: {}", body);

        var resultInfo = dockerService.startContainer(body);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("body", body);
        modelAndView.addObject("containerId", resultInfo.id());
        modelAndView.addObject("containerName", resultInfo.name());

        return modelAndView;
    }

    @PostMapping("/stop")
    public ModelAndView stopContainer(@RequestParam(name = "containerId") String containerId) {
        log.info("stopContainer: {}", containerId);

        dockerService.stopContainer(containerId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("stopMessage");
        modelAndView.addObject("containerId", containerId);
        return modelAndView;
    }
}
