package com.rok.faker.performanceapirest.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;


@Service
public class TesseractService {

    @Autowired
    private ITesseract tesseract;

    public String extractTestFromImage(String imagePath) {
        try {
            BufferedImage ipimage = ImageIO.read(new File(imagePath));
            double d = ipimage.getRGB(ipimage.getTileWidth() / 2, ipimage.getTileHeight() / 2);
            BufferedImage opimage = null;
            if (d >= -1.4211511E7 && d < -7254228) {
                opimage = processImg(ipimage, 3f, -10f);
            }
            else if (d >= -7254228 && d < -2171170) {
                opimage = processImg(ipimage, 1.455f, -47f);
            }
            else if (d >= -2171170 && d < -1907998) {
                opimage = processImg(ipimage, 1.35f, -10f);
            }
            else if (d >= -1907998 && d < -257) {
                opimage = processImg(ipimage, 1.19f, 0.5f);
            }
            else if (d >= -257 && d < -1) {
                opimage = processImg(ipimage, 1f, 0.5f);
            }
            else if (d >= -1 && d < 2) {
                opimage = processImg(ipimage, 1f, 0.35f);
            } else {
                opimage = processImg(ipimage, 1f, 0.01f);
            }
            return tesseract.doOCR(opimage);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage processImg(BufferedImage originalImage, float scaleFactor, float offset)
            throws IOException, TesseractException {
        int newWidth = originalImage.getWidth() * 2;
        int newHeight = originalImage.getHeight() * 2;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        resizedImage.createGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);

        // Convertir la imagen a blanco y negro
        BufferedImage blackAndWhiteImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                Color color = new Color(resizedImage.getRGB(x, y));
                int grayscale = (int) (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());
                int blackAndWhite = grayscale < 128 ? 0 : 255;
                blackAndWhiteImage.setRGB(x, y, new Color(blackAndWhite, blackAndWhite, blackAndWhite).getRGB());
            }
        }

        // performing scaling
        // and writing on a .jpg file
        ImageIO.write(blackAndWhiteImage, "jpg", new File("/home/pedro/Descargas/output.jpg"));

        return blackAndWhiteImage;
    }

}
