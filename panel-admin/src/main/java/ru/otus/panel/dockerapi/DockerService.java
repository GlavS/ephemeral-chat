package ru.otus.panel.dockerapi;

import ru.otus.panel.controller.StartDto;

public interface DockerService {
    ContainerInfo startContainer(StartDto data);

    void stopContainer(String containerId);
}
