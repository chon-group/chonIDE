package group.chon.ide.news.domain.devcontext;

import group.chon.ide.news.domain.model.file.Project;
import group.chon.ide.news.domain.model.file.embeddedmas.environment.exogenous.Firmware;
import group.chon.ide.news.domain.resourceaccess.LocalCommandPromptRunner;
import group.chon.ide.news.domain.resourceaccess.CommandPromptRunner;
import group.chon.ide.news.domain.resourceaccess.file.FileRepository;
import group.chon.ide.news.domain.resourceaccess.file.LocalFileRepository;
import group.chon.ide.news.domain.resourceaccess.ssh.JschSshHandler;
import group.chon.ide.news.domain.service.chonos.ChonosContext;
import group.chon.ide.news.domain.service.file.path.ProjectPathHandlerManager;
import group.chon.ide.news.domain.service.file.project.StandardProjectService;
import group.chon.ide.news.domain.service.file.projectfile.AgentFileService;
import group.chon.ide.news.domain.service.file.projectfile.IntercepterProjectFileService;
import group.chon.ide.news.domain.service.file.projectfile.StandardProjectFileService;
import group.chon.ide.news.domain.service.mindapi.MindAPIService;
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
        MindAPIService mindApi = new MindAPIService(this.targetHost);

        // Chonos Context
        CommandPromptRunner commandPromptRunner;
        if (this.isLocalAccess()) {
            commandPromptRunner = new JschSshHandler(this.user, this.password, this.targetHost);
        } else {
            commandPromptRunner = new LocalCommandPromptRunner();
        }
        ChonosContext chonosContext = new ChonosContext(commandPromptRunner);

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
