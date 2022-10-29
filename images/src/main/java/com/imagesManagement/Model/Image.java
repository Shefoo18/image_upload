package com.imagesManagement.Model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false, length = 450)
	private String description;

	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;

	@Column(name = "category", nullable = false)
	private Integer category;

	@Column(name = "status", nullable = true)
	private Boolean status;

	@Transient
	public String getPhotosImagePath() {
		if (name == null || id == null)
			return null;

		return "/user-images/" + name;
	}

}
