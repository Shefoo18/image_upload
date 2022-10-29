package com.imagesManagement.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.imagesManagement.Model.Image;
import com.imagesManagement.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;

	public void saveImage(Image image) {
		imageRepository.save(image);
	}

	public List<Image> getAllImages() {
		return imageRepository.findAll();
	}

	public List<Image> getNonProcessedImages() {
		return imageRepository.findNonProcessedImages();
	}
	
	public List<Image> getAcceptedImages() {
		return imageRepository.findAcceptedImages();
	}

	public Optional<Image> getImageById(Long id) {
		return imageRepository.findById(id);
	}

	public void acceptImage(Long id) {
		Optional<Image> image = imageRepository.findById(id);
		if (image.isPresent()) {
			image.get().setStatus(true);
		}
	}
	
	public Image rejectImage(Long id) {
		Optional<Image> image = imageRepository.findById(id);
		if (image.isPresent()) {
			image.get().setStatus(false);
		}
		return image.get();
	}
}
