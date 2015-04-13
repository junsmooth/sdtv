package com.potevio.sdtv.device.minigps;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class MiniGPS {
	private final static String verifyURl = "http://www.minigps.net/validatecodeservlet.do";
	private final static String gpsURL = "http://www.minigps.net/map/google/location";
	private static Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
	private static BufferedImage parsedImage = null;
	static {
		loadTrainData();
	}

//	public static void main(String[] args) throws Exception {
//		MiniGPSRequest req = new MiniGPSRequest();
//		CellTower ct = new CellTower("460", "0", "9779", "3721");
//		req.addCellTower(ct);
//		MiniGPSResult rst=getGPS(req);
//		System.out.println(rst);
//	}

	public static MiniGPSResult getGPS(MiniGPSRequest gpsRequest) throws Exception {
		MiniGPSResult result = null;
		int retryTimes = 0;
		while (true) {
			retryTimes++;
			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpClient = HttpClients.custom()
					.setDefaultCookieStore(cookieStore).build();
			HttpUriRequest request = RequestBuilder.get().setUri(verifyURl)
					.build();
			InputStream is = httpClient.execute(request).getEntity()
					.getContent();
			String verifyCode = parseVerifyCode(is);
			gpsRequest.setVerifycode(verifyCode);
			saveVerifyCodeImg(verifyCode);
			HttpEntity entity = new StringEntity(JSON.toJSONString(gpsRequest));
			request = RequestBuilder.post(gpsURL).setEntity(entity).build();
			HttpEntity returnEntity = httpClient.execute(request).getEntity();
			String str = EntityUtils.toString(returnEntity);
			if (str != null && str.contains("verify code error")
					&& retryTimes < 3) {
				continue;
			}
			result = JSON.toJavaObject(JSON.parseObject(str), MiniGPSResult.class);
			break;
		}
		return result;
	}

	private static void saveVerifyCodeImg(String verifyCode) throws IOException {
		String path = MiniGPS.class.getClassLoader().getResource("").getPath();
		File file = new File(path + File.separator + "temp" + File.separator
				+ verifyCode + ".jpg");
		ImageIO.write(parsedImage, "JPG", file);
	}

	private static String parseVerifyCode(InputStream is) throws Exception {
		List<BufferedImage> listImg = splitImage(is);
		// 2. split code
		// Map<BufferedImage, String> map = loadTrainData();
		// 3. parse code get result
		String result = "";
		for (BufferedImage bi2 : listImg) {
			result += getSingleCharOcr(bi2, map);
		}
		return result;
	}

	private static String getSingleCharOcr(BufferedImage img,
			Map<BufferedImage, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int size = bi.getHeight() * bi.getWidth();
			if (size != width * height) {
				continue;
			}
			int count = 0;
			Label1: for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {

					try {
						if (whiteThan(img.getRGB(x, y), 400) != whiteThan(
								bi.getRGB(x, y), 400)) {
							count++;
							if (count >= min)
								break Label1;
						}
					} catch (Exception e) {
						count = min;
						break Label1;
					}
				}
			}
			if (count < min) {
				result = map.get(bi);
				min = count;
			}
		}
		return result;
	}

	private static boolean whiteThan(int colorInt, int threshold) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > threshold) {
			return true;
		}
		return false;
	}

	private static List<BufferedImage> splitImage(InputStream is) {

		try {
			BufferedImage img = ImageIO.read(is);
			removeBackground(img, 400);
			changeSideWhite(img);

			parsedImage = img;

			return splitImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<BufferedImage> splitImage(BufferedImage img)
			throws IOException {
		List<BufferedImage> subImages = new ArrayList();
		int w = img.getWidth();
		int h = img.getHeight();
		int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
		for (int i = 0; i < w; i++) {
			int black = 0;
			for (int j = 0; j < h; j++) {
				int color = img.getRGB(i, j);
				// black
				if (!whiteThan(img.getRGB(i, j), 400)) {
					black++;
					if (x1 == -1) {
						x1 = i;
					}
					if (x2 == -1) {
						x2 = i;
					} else {
						if (x2 < i) {
							x2 = i;
						}
					}
					if (y1 == -1) {
						y1 = j;
					} else {
						if (y1 > j) {
							y1 = j;
						}
					}
					if (y2 == -1) {
						y2 = j;
					} else {
						if (y2 < j) {
							y2 = j;
						}
					}

				}

			}
			if (black == 0) {
				if (x1 != -1) {
					BufferedImage bis = img.getSubimage(x1 - 1, y1 - 1, x2 - x1
							+ 2, y2 - y1 + 2);
					subImages.add(bis);
					x1 = -1;
					x2 = -1;
					y1 = -1;
					y2 = -1;
				}
			}
		}
		return subImages;
	}

	private static void changeSideWhite(BufferedImage bi) {
		int w = bi.getWidth();
		int h = bi.getHeight();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (i < 1 || j < 1 || i > w - 2 || j > h - 2) {
					bi.setRGB(i, j, 0xFFFFFF);
				}
			}
		}

	}

	private static void removeBackground(BufferedImage img, int threshold)
			throws IOException {

		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (whiteThan(img.getRGB(x, y), threshold)) {
					img.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					img.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
	}

	private static void loadTrainData() {
		String path = MiniGPS.class.getClassLoader().getResource("").getPath();
		// classes/
		File dir = new File(path + File.separator + "train");
		File[] files = dir.listFiles();
		for (File file : files) {
			try {
				map.put(ImageIO.read(file), file.getName().charAt(0) + "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
