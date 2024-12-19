package group.chon.ide.news.domain.model.embeddedmas.masconfiguration;

import group.chon.ide.news.domain.model.ProjectFile;
import group.chon.ide.news.domain.model.embeddedmas.agent.Agent;

public interface MasConfiguration extends ProjectFile {

    void saveAgent(Agent agent);

    Agent getAgent(String name);

    void removeAgent(Agent agent);
}
