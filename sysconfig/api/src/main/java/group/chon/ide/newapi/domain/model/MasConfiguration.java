package group.chon.ide.newapi.domain.model;

public interface MasConfiguration extends ProjectFile {

    void saveAgent(Agent agent);

    void removeAgent(Agent agent);
}
