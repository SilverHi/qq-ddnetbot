package com.silver.ddrtools.common.util;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {
    public static String saveImage(String imgurl,String path)  {
        URL url = null;
        try {
            url = new URL(imgurl);

        InputStream inputStream = url.openStream();
        BufferedImage image = ImageIO.read(inputStream);
        UUID uuid = UUID.randomUUID();
        String filePath = path+uuid.toString()+".png";
        ImageIO.write(image, "png", Files.newOutputStream(Paths.get(filePath))); // 保存成png图片
        inputStream.close();
        return filePath;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
