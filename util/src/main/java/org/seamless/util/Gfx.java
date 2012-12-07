 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.seamless.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Parts of this source:
 * http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
 *
 * @author Christian Bauer
 */
public class Gfx {

    public static byte[] resizeProportionally(ImageIcon icon, String contentType,
                                              int newWidth, int newHeight) throws IOException {

        // Scale it as wide or as high as possible, within the given constraints
        double widthRatio = newWidth != icon.getIconWidth()
                ? (double) newWidth / icon.getIconWidth()
                : 1.0;

        double heightRatio = newHeight != icon.getIconHeight()
                ? (double) newHeight / icon.getIconHeight()
                : 1.0;

        if (widthRatio < heightRatio) {
            newHeight = (int) (icon.getIconHeight() * widthRatio);
        } else {
            newWidth = (int) (icon.getIconWidth() * heightRatio);
        }

        int imageType = "image/png".equals(contentType)
                ? BufferedImage.TYPE_INT_ARGB
                : BufferedImage.TYPE_INT_RGB;

        BufferedImage bImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), imageType);
        Graphics2D g2d = bImg.createGraphics();
        g2d.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
        g2d.dispose();

        BufferedImage scaledImg =
                Gfx.getScaledInstance(bImg, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR, false);

        String formatName = "";
        if ("image/png".equals(contentType))
            formatName = "png";
        else if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType))
            formatName = "jpeg";

        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ImageIO.write(scaledImg, formatName, baos);
        return baos.toByteArray();
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img           the original image to be scaled
     * @param targetWidth   the desired width of the scaled instance,
     *                      in pixels
     * @param targetHeight  the desired height of the scaled instance,
     *                      in pixels
     * @param hint          one of the rendering hints that corresponds to
     *                      {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *                      {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *                      {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *                      {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *                      scaling technique that provides higher quality than the usual
     *                      one-step technique (only useful in downscaling cases, where
     *                      {@code targetWidth} or {@code targetHeight} is
     *                      smaller than the original dimensions, and generally only when
     *                      the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    static public BufferedImage getScaledInstance(BufferedImage img,
                                                  int targetWidth,
                                                  int targetHeight,
                                                  Object hint,
                                                  boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
