/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.github.jipsg.twelvemonkeys;

import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Load various images.
 */
public class ImageConversionTwelveMonkeysTest extends BaseTwelveMonkeysTest {

    @Before
    public void setup() {
        super.setup();
    }

    // ======================================================================
    // Image format conversion
    // ======================================================================

    @Test
    public void testImageWriteAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        // sourceImageFileList.add(getImageFile("bmp", "marble.bmp"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));
        // sourceImageFileList.add(getImageFile("jp2", "marble.jp2"));
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));

        for (File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testImageWriteAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    @Test
    public void testImageWriteAsPng() throws Exception {

        String formatName = "png";
        List<File> sourceImageFileList = new ArrayList<File>();

        // sourceImageFileList.add(getImageFile("bmp", "marble.bmp"));
        sourceImageFileList.add(getImageFile("gif", "marble.gif"));
        // sourceImageFileList.add(getImageFile("jp2", "marble.jp2"));
        sourceImageFileList.add(getImageFile("jpg", "marble.jpg"));
        sourceImageFileList.add(getImageFile("png", "marble.png"));
        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));

        for (File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testImageWriteAsPng", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // JPEG CMYK Images
    // ======================================================================

    /**
     * Process the JPEGs with CMYK color space and store them as JPEG again.
     */
    @Test
    public void testProcessCMYKImages() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("jpg", "test-image-cmyk-lzw.jpg"));
        sourceImageFileList.add(getImageFile("jpg", "test-image-cmyk-uncompressed.jpg"));

        for (File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testProcessCMYKImages", sourceImageFile, formatName);
            writeBufferedImage(resample(bufferedImage, 320, 320), formatName, targetImageFile);
        }
    }

    // ======================================================================
    // Transparent Images
    // ======================================================================

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Without
     * further handling the alpha-channel will be rendered black or as a red tint.
     */
    @Test
    public void testWriteTransparentImagesAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("gif", "test-image-transparent.gif"));
        sourceImageFileList.add(getImageFile("png", "test-image-transparent.png"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            assertTrue("Expecting transparency", bufferedImage.getColorModel().hasAlpha());
            assertTrue("Expecting non-RGB color model", bufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR || bufferedImage.getType() == BufferedImage.TYPE_BYTE_INDEXED);

            File targetImageFile = createOutputFileName("testWriteTransparentImagesAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }

    /**
     * Convert images having a transparency layer (alpha-channel) to JPG. Remove
     * the alpha-channel (ARGB) by painting the image into an RGB image thereby
     * removing the fourth channel and transparency.
     */
    @Test
    public void testWriteTransparentImagesUsingRGBAsJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("gif", "test-image-transparent.gif"));
        sourceImageFileList.add(getImageFile("png", "test-image-transparent.png"));

        for (File sourceImageFile : sourceImageFileList) {

            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            assertTrue("Expecting transparency", bufferedImage.getColorModel().hasAlpha());
            assertTrue("Expecting non-RGB color model", bufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR || bufferedImage.getType() == BufferedImage.TYPE_BYTE_INDEXED);

            BufferedImage rgbBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = rgbBufferedImage.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);
            graphics.dispose();
            assertValidBufferedImage(rgbBufferedImage);
            assertFalse("Expecting no transparency", rgbBufferedImage.getColorModel().hasAlpha());
            assertEquals("Expecting RGB color model", BufferedImage.TYPE_INT_RGB, rgbBufferedImage.getType());

            File targetImageFile = createOutputFileName("testWriteTransparentImagesUsingRGBAsJpeg", sourceImageFile, formatName);
            writeBufferedImage(rgbBufferedImage, formatName, targetImageFile);
        }
    }

    // ======================================================================
    // TIFF Images
    // ======================================================================

    @Test
    public void testConvertTiffImagesToJpeg() throws Exception {

        String formatName = "jpeg";
        List<File> sourceImageFileList = new ArrayList<File>();

        sourceImageFileList.add(getImageFile("tiff", "marble.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-multi-gray-compression-type-4.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-multi-rgb-compression-type-7.tiff"));
        // sourceImageFileList.add(getImageFile("tiff", "test-single-cmyk-compression-lzw.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-single-gray-compression-lzw.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-single-gray-compression-type-2.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-single-gray-compression-type-3.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-single-gray-compression-type-4.tiff"));
        sourceImageFileList.add(getImageFile("tiff", "test-single-rgb-uncompressed.tiff"));

        for (File sourceImageFile : sourceImageFileList) {
            BufferedImage bufferedImage = createBufferedImage(sourceImageFile);
            assertValidBufferedImage(bufferedImage);
            File targetImageFile = createOutputFileName("testConvertTiffImageWriteToJpeg", sourceImageFile, formatName);
            writeBufferedImage(bufferedImage, formatName, targetImageFile);
        }
    }
}
