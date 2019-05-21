package com.argcv.gpm.entity;

import com.argcv.gpm.util.OtsuThresholder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageInfo {
    public static final Integer spSliceNum = 8; //
    private File f = null;
    private BufferedImage bi;
    private boolean success = true;
    private Integer width = null;
    private Integer height = null;
    private Integer[][][] hslmtx; // color area matrix
    private byte[] otsumtx; // Otsu method
    private Double[][][] colorHistogram;

    /**
     * RGB => HSL or HSV , H in [0,360) s,l,v in [0,100)
     *
     * @author Yu
     */
    public class ColorChange {
        int[] rgba;
        int[] hsla;
        int[] hsva;
        double luminance;

        public ColorChange() {

        }

        public ColorChange(int rgb) {
            rgb2a(rgb);
            rgba2hslv();
            setLuminance();
        }

        public void setRgba(int rgb) {
            rgb2a(rgb);
            rgba2hslv();
            setLuminance();
        }

        private void setLuminance() {
            if (rgba != null && rgba.length == 3) {
                luminance = 0.2126 * rgba[0] + 0.7152 * rgba[1] + 0.0722
                        * rgba[2];
            }
        }

        private void rgb2a(int rgb) {
            rgba = new int[3];
            rgba[2] = rgb & 0xff;
            rgb /= 0xFF;
            rgba[1] = rgb & 0xff;
            rgb /= 0xFF;
            rgba[0] = rgb & 0xff;
        }

        private void rgba2hslv() {
            hsla = new int[3];
            hsva = new int[3];
            double h = 0;
            double l = 0;
            double s = 0;
            double v = 0;
            double r = (double) rgba[0] / 256;
            double g = (double) rgba[1] / 256;
            double b = (double) rgba[2] / 256;
            double max = r > g ? (r > b ? r : b) : (g > b ? g : b);
            double min = r < g ? (r < b ? r : b) : (g < b ? g : b);
            if (max == min) {
                h = 0;
            } else if (max == r && g >= b) {
                h = (double) ((60 * (double) (g - b) / (max - min) + 0));
            } else if (max == r && g < b) {
                h = (double) ((60 * (double) (g - b) / (max - min) + 360));
            } else if (max == g) {
                h = (double) ((60 * (double) (b - r) / (max - min) + 120));
            } else if (max == b) {
                h = (double) ((60 * (double) (r - g) / (max - min) + 240));
            }
            l = (double) ((max + min) / 2);
            if (l == 0 || max == min) {
                s = 0;
            } else if (0 < l && l <= 0.5) {
                s = (double) ((max - min) / (2 * l));
            } else if (l > 0.5) {
                s = (double) ((max - min) / (2 - 2 * l));
            }
            // System.out.println("h :" + h + " s :" + s + " l :" + l);
            hsla[0] = (int) h;
            hsla[1] = (int) (s * 100);
            hsla[2] = (int) (l * 100);

            if (0 == max) {
                s = 0;
            } else {
                s = (double) (1 - min / max);
            }
            v = (int) max;
            hsva[0] = (int) h;
            hsva[1] = (int) (s * 100);
            hsva[2] = (int) (v * 100);
        }

        public int[] getRgba() {
            return rgba;
        }

        public void setRgba(int[] rgba) {
            this.rgba = rgba;
        }

        public int[] getHsla() {
            return hsla;
        }

        public void setHsla(int[] hsla) {
            this.hsla = hsla;
        }

        public int[] getHsva() {
            return hsva;
        }

        public void setHsva(int[] hsva) {
            this.hsva = hsva;
        }

        public double getLuminance() {
            return luminance;
        }

        public void setLuminance(double luminance) {
            this.luminance = luminance;
        }

    }

    public ImageInfo(String fileName) {
        f = new File(fileName);
        if (f.exists()) {
            try {
                bi = ImageIO.read(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            success = true;
            basicCal();
        } else {
            success = false;
        }
    }

    public ImageInfo(File f) {
        this.f = f;
        if (f.exists()) {
            try {
                bi = ImageIO.read(f);
                basicCal();
                success = true;
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        } else {
            success = false;
        }
    }

    public ImageInfo(InputStream f) {
        if (f != null) {
            try {
                bi = ImageIO.read(f);
                basicCal();
                success = true;
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        } else {
            success = false;
        }
    }

    private void basicCal() {
        if (success) {
            width = bi.getWidth();
            height = bi.getHeight();
            hslmtx = new Integer[spSliceNum][spSliceNum][3];
            colorHistogram = new Double[4][4][4];
            ColorChange cc = new ColorChange();
            Integer[][][] hslData = new Integer[spSliceNum][spSliceNum][4]; // h
            // s
            // l
            // size
            Integer[] lumiData = new Integer[spSliceNum * spSliceNum];
            int wPerSlice = 1 + width / spSliceNum;
            int hPerSlice = 1 + height / spSliceNum;
            for (int x = 0; x < spSliceNum; x++) {
                for (int y = 0; y < spSliceNum; y++) {
                    for (int z = 0; z < 4; z++) {
                        hslData[x][y][z] = 0;
                    }
                    lumiData[x * spSliceNum + y] = 0;
                }
            }
            Integer[][][] colorHistogramPoints = new Integer[4][4][4];// 0~63,64~127,128~191,192~255
            Integer colorHistogramPointsMaxValue = 0;
            for (int r = 0; r < 4; r++) {
                for (int g = 0; g < 4; g++) {
                    for (int b = 0; b < 4; b++) {
                        colorHistogramPoints[r][g][b] = 0;
                    }
                }
            }

            for (int w = 0; w < width; w++) {
                for (int h = 0; h < height; h++) {
                    cc.setRgba(bi.getRGB(w, h));
                    int[] rgb = cc.getRgba();
                    for (int i = 0; i < 3; i++) {
                        if (rgb[i] >= 0 && rgb[i] <= 63) {
                            rgb[i] = 0;
                        } else if (rgb[i] >= 64 && rgb[i] <= 127) {
                            rgb[i] = 1;
                        } else if (rgb[i] >= 128 && rgb[i] <= 191) {
                            rgb[i] = 2;
                        } else {
                            rgb[i] = 3;
                        }
                    }
                    colorHistogramPoints[rgb[0]][rgb[1]][rgb[2]]++;
                    if (colorHistogramPoints[rgb[0]][rgb[1]][rgb[2]] > colorHistogramPointsMaxValue)
                        colorHistogramPointsMaxValue = colorHistogramPoints[rgb[0]][rgb[1]][rgb[2]];

                    int[] hsl = cc.getHsla();

                    if (hsl != null) {
                        int wp = w / wPerSlice;
                        int hp = h / hPerSlice;
                        hslData[wp][hp][0] += hsl[0];
                        hslData[wp][hp][1] += hsl[1];
                        hslData[wp][hp][2] += hsl[2];
                        hslData[wp][hp][3]++;

                        lumiData[wp * spSliceNum + hp] += (int) cc.getLuminance();

                    } else {
                        System.out.println("error in :" + w + ":" + h);
                    }
                }
            }
            for (int r = 0; r < 4; r++) {
                for (int g = 0; g < 4; g++) {
                    for (int b = 0; b < 4; b++) {
                        //colorHistogramPoints[r][g][b] = 0;
                        colorHistogram[r][g][b] = (double) colorHistogramPoints[r][g][b] / colorHistogramPointsMaxValue;
                    }
                }
            }
            int[] hslSumData = new int[4];
            for (int x = 0; x < spSliceNum; x++) {
                for (int y = 0; y < spSliceNum; y++) {
                    for (int z = 0; z < 4; z++) {
                        hslSumData[z] += hslData[x][y][z];
                    }
                }
            }
            for (int z = 0; z < 3; z++) {
                if (hslSumData[3] == 0)
                    hslSumData[3] = 1;
                hslSumData[z] /= hslSumData[3]; // get the average
            }
            for (int x = 0; x < spSliceNum; x++) {
                for (int y = 0; y < spSliceNum; y++) {
                    for (int z = 0; z < 3; z++) {
                        if (hslData[x][y][3] == 0)
                            hslData[x][y][3] = 1;
                        hslData[x][y][z] = hslData[x][y][z] / hslData[x][y][3];
                        hslmtx[x][y][z] = hslData[x][y][z] >= hslSumData[z] ? 1
                                : 0;

                    }
                    lumiData[x * spSliceNum + y] /= hslData[x][y][3];
                }
            }


            OtsuThresholder thresholder = new OtsuThresholder();
            otsumtx = new byte[spSliceNum * spSliceNum];
            byte[] srcData = new byte[spSliceNum * spSliceNum];
            for (int x = 0; x < spSliceNum; x++) {
                for (int y = 0; y < spSliceNum; y++) {
                    srcData[x * spSliceNum + y] = (byte) (lumiData[x * spSliceNum + y] & 0xff);
                }
            }
            //int threshold = thresholder.doThreshold(srcData ,otsumtx);
            thresholder.doThreshold(srcData, otsumtx);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public BufferedImage getBi() {
        return bi;
    }

    public void setBi(BufferedImage bi) {
        this.bi = bi;
    }

    public Integer[][][] getHslmtx() {
        return hslmtx;
    }

    public void setHslmtx(Integer[][][] hslmtx) {
        this.hslmtx = hslmtx;
    }

    public void setSuccess(boolean isSuccess) {
        this.success = isSuccess;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public Double[][][] getColorHistogram() {
        return colorHistogram;
    }

    public void setColorHistogram(Double[][][] colorHistogram) {
        this.colorHistogram = colorHistogram;
    }

    public static Integer getSpslicenum() {
        return spSliceNum;
    }

    public byte[] getOtsumtx() {
        return otsumtx;
    }

    public void setOtsumtx(byte[] otsumtx) {
        this.otsumtx = otsumtx;
    }

    public static void main(String[] args) {
    }
}
