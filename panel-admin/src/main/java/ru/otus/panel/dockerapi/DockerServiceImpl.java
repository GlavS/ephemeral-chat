package ru.otus.panel.dockerapi;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.panel.controller.StartDto;

@Service
public class DockerServiceImpl implements DockerService {

    private final DockerClient dockerClient;

    public DockerServiceImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public ContainerInfo startContainer(StartDto data) {
        var tcp12443 = ExposedPort.tcp(12443); // port inside container
        Ports portBindings = new Ports();
        portBindings.bind(
                tcp12443, Ports.Binding.bindPort(Integer.parseInt(data.chatPort()))); // binding is port exposed outside
        List<String> environments = List.of("CHAT_USERNAME=" + data.chatUsername(), "CHAT_PASSWORD=" + data.password());

        Volume volume = new Volume("/home/sergey/tmp"); // inside container
        Bind volumeBind = new Bind("/home/sergey/cert", volume); // mount point outside

        CreateContainerResponse containerResponse = dockerClient
                .createContainerCmd("glavs/ephemeral-chat")
                .withExposedPorts(tcp12443)
                .withHostConfig(HostConfig.newHostConfig()
                        .withPortBindings(portBindings)
                        .withBinds(volumeBind))
                .withEnv(environments)
                .exec();
        dockerClient.startContainerCmd(containerResponse.getId()).exec();
        var containerId = containerResponse.getId();
        InspectContainerResponse inspection =
                dockerClient.inspectContainerCmd(containerId).exec();
        String name = inspection.getName();
        return new ContainerInfo(containerId, name);
    }

    @Override
    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
        dockerClient.removeContainerCmd(containerId).exec();
    }
}
