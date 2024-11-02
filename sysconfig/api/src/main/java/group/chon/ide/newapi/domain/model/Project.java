package group.chon.ide.newapi.domain.model;

import lombok.Data;

@Data
public class Project {

    private String name;

    private ReasoningLayer reasoningLayer;

    private ExogenousEnvLayer exogenousEnvLayer;

    private MasConfiguration masConfiguration;

}
