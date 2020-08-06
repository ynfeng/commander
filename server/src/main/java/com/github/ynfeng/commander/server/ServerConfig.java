package com.github.ynfeng.commander.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ServerConfig {
    private String name;
    private Address address;
    private List<StartStep> startSteps = Lists.newArrayList();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void addStartStep(StartStep startStep) {
        startSteps.add(startStep);
    }

    public List<StartStep> getStartSteps() {
        return startSteps;
    }
}