package group.chon.ide.news.domain.service.project;

import group.chon.ide.news.domain.model.Project;

import java.util.List;

/**
 * Serviço de um projeto ChonIDE.
 */
public interface ProjectService {

    /**
     * Retorna todos os projetos do sistema.
     *
     * @return Projetos do sistema.
     */
    List<Project> getAll();

    /**
     * Retorna um projeto completo através do seu nome.
     *
     * @param name Nome do projeto.
     * @return Projeto.
     */
    Project getByName(String name);

    /**
     * Cria um projeto.
     *
     * @param project Projeto a ser criado.
     */
    void create(Project project);

    /**
     * Renomeia um projeto.
     *
     * @param project Projeto.
     * @param newName Novo nome do projeto.
     */
    void rename(Project project, String newName);

    /**
     * Deleta o projeto através do seu nome.
     *
     * @param name Nome do projeto.
     */
    void delete(String name);

}
