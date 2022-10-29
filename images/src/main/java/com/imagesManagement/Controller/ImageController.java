package com.imagesManagement.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.imagesManagement.Model.Image;
import com.imagesManagement.Service.ImageService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/image")
public class ImageController {

	@Value("${uploadDir}")
	private String uploadFolder;

	private final ImageService imageService;

	@GetMapping("/")
	public String goToCreateImage(Model model) {
		model.addAttribute("image", new Image());
		return "createImage";
	}

	@PostMapping("/createImage")
	public String createImage(@ModelAttribute("image") Image image, Model model,
			@RequestParam("imagee") MultipartFile file) throws IOException {

		if (validateImage(image, file)) {
			return "redirect:/image/?nullFeild";
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		image.setName(fileName);
		image.setImage(file.getBytes());
		imageService.saveImage(image);
		saveFile(uploadFolder, fileName, file);

		return "redirect:/image/?success";
	}

	public void saveFile(String uploadFolder, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadFolder);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	public boolean validateImage(Image image, MultipartFile file) {
		if (image.getDescription() == null || image.getDescription().isBlank() || image.getCategory() == null
				|| file.isEmpty()) {
			return true;
		}

		return false;
	}

	@GetMapping("/display/{id}")
	@ResponseBody
	public void showImage(@PathVariable("id") Long id, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<Image> image = imageService.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(image.get().getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/accept/{id}")
	public String acceptImage(@PathVariable("id") Long id) {
		imageService.acceptImage(id);
		return "redirect:/admin";
	}

	@GetMapping("/reject/{id}")
	public String rejectImage(@PathVariable("id") Long id) throws IOException {
		Image image = imageService.rejectImage(id);
		Path fileToDeletePath = Paths.get(uploadFolder + "/" + image.getName());
		File file = new File(fileToDeletePath.toString());
		if (file.exists()) {
			Files.delete(fileToDeletePath);
		}

		return "redirect:/admin";
	}

}
