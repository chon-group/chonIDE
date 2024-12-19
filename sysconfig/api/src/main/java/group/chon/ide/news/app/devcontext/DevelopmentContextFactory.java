package group.chon.ide.news.app.devcontext;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.model.embeddedmas.environment.exogenous.Firmware;
import group.chon.ide.news.infra.accessmethods.LocalCommandPromptRunner;
import group.chon.ide.news.app.accessmethods.CommandPromptRunner;
import group.chon.ide.news.domain.repository.FileRepository;
import group.chon.ide.news.infra.repository.LocalFileRepository;
import group.chon.ide.news.infra.accessmethods.JschSshHandler;
import group.chon.ide.news.app.service.chonos.ChonosModules;
import group.chon.ide.news.domain.service.path.ProjectPathHandlerManager;
import group.chon.ide.news.domain.service.project.StandardProjectService;
import group.chon.ide.news.domain.service.projectfile.AgentFileService;
import group.chon.ide.news.domain.service.projectfile.IntercepterProjectFileService;
import group.chon.ide.news.domain.service.projectfile.StandardProjectFileService;
import group.chon.ide.news.app.service.mindapi.MindAPIService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DevelopmentContextFactory {

    private final String targetHost;

    private final String user;

    private final String password;

    public static void main(String[] args) {
        DevelopmentContextFactory developmentContextFactory = new DevelopmentContextFactory("localhost", "root", "root");
        DevelopmentContext devContext = developmentContextFactory.make();

        Project project = StandardProjectService.mountDefaultProject("meuprojeto");
        devContext.projects().create(project);

        devContext.files().save(project, new Firmware("sketch", ""));

        devContext.files().save(project, new Firmware("bob", ".bonito"));
        devContext.files().save(project, new Firmware("kate", ".bonita"));

        String projectPath = ProjectPathHandlerManager.getInstance().getProjectPathHandler().mountProjectPath(project);

        devContext.chonos().embeddedMas().importMas(projectPath);
        devContext.chonos().embeddedMas().start();

        Object bobState = devContext.mindApi().getAgentState("bob", 1);
        System.out.println(bobState);

        devContext.chonos().embeddedMas().stop();

        devContext.chonos().connection().forgetAllNetworks();

        devContext.chonos().connection().apMode("gabriel", "24821236");

    }

    public DevelopmentContext make() {
        // Mind API Context
        MindAPIService mindApi = new MindAPIService(null, null, this.targetHost);

        // Chonos Context
        CommandPromptRunner commandPromptRunner;
        if (this.isLocalAccess()) {
            commandPromptRunner = new JschSshHandler(this.user, this.password, this.targetHost);
        } else {
            commandPromptRunner = new LocalCommandPromptRunner();
        }
        ChonosModules chonosContext = new ChonosModules(commandPromptRunner);

        // Project files Context
        FileRepository fileRepository = new LocalFileRepository();
        StandardProjectFileService standardProjectFileService = new StandardProjectFileService(fileRepository);
        AgentFileService agentFileService = new AgentFileService(standardProjectFileService);
        IntercepterProjectFileService intercepterProjectFileService = new IntercepterProjectFileService(standardProjectFileService, agentFileService);

        StandardProjectService standardProjectService = new StandardProjectService(intercepterProjectFileService);

        return new DevelopmentContext(chonosContext, mindApi, standardProjectService, intercepterProjectFileService);
    }

    private boolean isLocalAccess() {
        return this.targetHost.equals("localhost") || this.targetHost.equals("127.0.0.1");
    }
}
