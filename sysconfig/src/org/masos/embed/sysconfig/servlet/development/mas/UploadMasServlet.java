package org.masos.embed.sysconfig.servlet.development.mas;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.controller.MasBuilderContentController;
import org.masos.embed.sysconfig.controller.MasStructureController;
import org.masos.embed.sysconfig.exception.ErrorCreatingFileException;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.model.dto.MasDTO;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;
import org.masos.embed.sysconfig.utils.FileUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/uploadMasServlet")
public class UploadMasServlet extends HttpServlet {


	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		MasDTO masDTO = new Gson().fromJson(request.getParameter("mas"), MasDTO.class);

		User user = (User) request.getSession().getAttribute("user");

		SSHConnection.getDefault(user).execute(ReasoningScriptManager.EMBEDDED_MAS_STOP);

		MasStructureController masStructureController = new MasStructureController(masDTO.getMasName(), masDTO.getAgents());

		File masFile = MasBuilderContentController.buildMas(masStructureController);
		if(masFile != null && masFile.exists()) {

			String returnMasImport = "";
			try {
				File masFileToUpload = new File("/tmp/mas_" + masFile.getName());
				if (!masFileToUpload.exists()) {
					masFileToUpload.createNewFile();
				}
				Files.copy(Files.newInputStream(masFile.toPath()), masFileToUpload.toPath(), StandardCopyOption.REPLACE_EXISTING);
				returnMasImport = SSHConnection.getDefault(user).execute(ReasoningScriptManager.mountEmbeddedMASImportScript(
						masFileToUpload.getAbsolutePath()));
			} catch (Exception e) {
				throw new ErrorCreatingFileException(masFile.getName(), e);
			}

			if (returnMasImport.contains("Archive") && returnMasImport.contains("creating") && returnMasImport.contains("inflating")) {
				List<String> exceptFilesToDelete = new ArrayList<>();
				exceptFilesToDelete.add(masFile.getName());
				exceptFilesToDelete.add(masStructureController.getMasName());
				FileUtils.deleteAllFilesExcept(FileUtils.MAS_BASE_PATH, exceptFilesToDelete);
			}
		}

		Response.build(response).json().ok(masDTO);
	}
	
}
