package com.graduation.ss.util;

public class PathUtil {
	private static String seperator = System.getProperty("file.separator");

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/projectdev/image";
		} else {
			basePath = "/home/SearchStreet/image";
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	public static String getShopImgPath(long shopId) {
		String imagePath = "/upload/images/item/shop/shopImg/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}

	public static String getShopBusinessLicenseImgPath(long shopId) {
		String imagePath = "/upload/images/item/shop/businessLicenseImg/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}

	public static String getShopProfileImgPath(long shopId) {
		String imagePath = "/upload/images/item/shop/profileImg/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}

	public static String getAppealImgPath(Long appealId) {
		String imagePath = "/upload/images/item/appeal/appealImg/" + appealId + "/";
		return imagePath.replace("/", seperator);
	}
	public static String getServiceImgPath(long serviceId) {
		String imagePath = "/upload/images/item/shop/serviceImg/" + serviceId + "/";
		return imagePath.replace("/", seperator);
	}
}
