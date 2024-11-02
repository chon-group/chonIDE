package group.chon.ide.news.domain.model.file.embeddedmas.masconfiguration;

import group.chon.ide.news.domain.model.file.ProjectFile;
import group.chon.ide.news.domain.model.file.embeddedmas.agent.Agent;

public interface MasConfiguration extends ProjectFile {

    void saveAgent(Agent agent);

    Agent getAgent(String name);

    void removeAgent(Agent agent);
}
