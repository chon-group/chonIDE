package group.chon.ide.news.domain.service.chonos;

import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;
import group.chon.ide.news.domain.model.chonos.Neighbor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChonosNeighborsService {

    public static final String LIST = "sudo chonos-neighbors --list";

    public static final String FORGET = "sudo chonos-neighbors --forget";

    private final CommandPromptRunner commandPromptRunner;

    public List<Neighbor> findAll() {
        return null;
    }

    public void forget() {

    }

}
