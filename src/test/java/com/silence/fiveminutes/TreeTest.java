package com.silence.fiveminutes;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @author 李晓冰
 * @date 2020年03月27日
 */
public class TreeTest {

    public static void main(String[] args) throws IOException {
        int width = 500, height = 500;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
        writeRect(bufferedImage, width, height);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);

        Font font = SourceHanSansCN(Font.PLAIN, 50);
        graphics.setFont(font);
        graphics.drawString("李晓冰", 100, 100);
        graphics.dispose();
        writeFile(bufferedImage);
    }

    public static void addFont(BufferedImage bufferedImage, Font font, String msg, int x, int y) {

    }

    public static Font SourceHanSansCN(Integer style, Integer size) {
        Font actionJsonBase = new Font(null, style, size);

        InputStream loadFile = TreeTest.class.getResourceAsStream("/SourceHanSansCN-Normal.ttf");
        try {
            Font createFont = Font.createFont(Font.TRUETYPE_FONT, loadFile);
            actionJsonBase = createFont.deriveFont(style, size);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actionJsonBase;
    }

    public static void writeRect(BufferedImage bufferedImage, int width, int height) {
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
    }

    public static void writeFile(BufferedImage bufferedImage) throws IOException {
        File file = new File("E:/a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ImageIO.write(bufferedImage, "jpg", fileOutputStream);
        fileOutputStream.close();
    }

}

@Data
class GradePrdRecord {
    private Integer prdId;
    private String grade;
    private BigDecimal gradePrice;
}