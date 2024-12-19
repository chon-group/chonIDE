package group.chon.ide.news.domain.model.embeddedmas.environment.exogenous;

import lombok.Data;

import java.util.List;

@Data
public class ExogenousEnv {

    private List<Firmware> firmwares;

    private List<Library> libraries;
}
