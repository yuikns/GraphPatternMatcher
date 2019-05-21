package com.argcv.gpm.module.index;

import com.argcv.gpm.entity.ImageInfo;
import com.argcv.gpm.service.ConfigService;
import com.argcv.gpm.util.analyzer.KeywordsAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

public class IndexBuilder {
    protected String baseDir = null;
    protected String indexDir = null;
    protected Analyzer analyzer = null;
    protected FSDirectory diskIndexFile = null;
    protected IndexWriterConfig config = null;
    protected IndexWriter iw = null;
    protected File indexFileDir;

    public IndexBuilder() {
        this.baseDir = ConfigService.get("index.baseDir");
        this.indexDir = "resource" + ConfigService.get("index.indexDir");
        analyzer = new KeywordsAnalyzer(); // may never used
        config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        boolean status = readAndBuild();
        System.out.println("read and build, status:" + status);
        // System.out.println("base dir :" + baseDir + "\nindex dir : " +
        // indexDir + "\n"+ ConfigService.osType);
    }

    protected boolean setIndexDir(String indexDir) {
        this.indexDir = indexDir;
        indexFileDir = new File(indexDir);
        try {
            diskIndexFile = FSDirectory.open(indexFileDir);
            iw = new IndexWriter(diskIndexFile, config);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        try {
            iw.close();
            diskIndexFile.close();
        } catch (IOException ignored) {
        }
    }

    private void idxBuild(File f) {
        String name = f.getName();
        System.out.println("name :" + name);
        ImageInfo ii = new ImageInfo(f);
        Document doc = null;
        doc = new Document();
        // Field id = new StringField("id", idVal, Field.Store.YES);
        // Field keywords = new TextField("keywords", keywordsVal,
        // Field.Store.YES);
        // doc.add(id);
        // doc.add(keywords);
        Integer[][][] hslMatrix = ii.getHslmtx();
        // Integer[][][] rgbMatrix = ii.getRgbmtx();
        byte[] otsu = ii.getOtsumtx();
        for (int w = 0; w < ImageInfo.spSliceNum; w++) {
            for (int h = 0; h < ImageInfo.spSliceNum; h++) {
                String otsuk = String.format("OTSU_%d_%d", w, h);
                Field otsuFeature = new StringField(otsuk,
                        String.valueOf(otsu[w * ImageInfo.spSliceNum + h]),
                        Field.Store.YES);
                doc.add(otsuFeature);
                for (int i = 0; i < 3; i++) {
                    String k = String.format("HSL_%d_%d_%d", w, h, i);
                    Field imgFeature = new StringField(k,
                            String.valueOf(hslMatrix[w][h][i]), Field.Store.YES);
                    doc.add(imgFeature);

                }
            }
        }
        Double[][][] colorHistogram = ii.getColorHistogram();
        for (int r = 0; r < 4; r++) {
            for (int g = 0; g < 4; g++) {
                for (int b = 0; b < 4; b++) {
                    String k = String.format("ColorHistogram_%d_%d_%d", r, g, b);
                    Field imgFeature = new DoubleField(k, colorHistogram[r][g][b], Field.Store.YES);
                    doc.add(imgFeature);
//					System.out.println(String.format("ADD : %s : %f",k,colorHistogram[r][g][b]));
                }
            }
        }
        // Field widthField = new IntField("width", ii.getWidth(),
        // Field.Store.YES);
        Field widthField = new IntField("width", ii.getWidth(), Field.Store.YES);
        doc.add(widthField);
        // Field heightField = new StringField("height", value, stored)
        Field heiField = new IntField("height", ii.getHeight(), Field.Store.YES);
        doc.add(heiField);
        Field pathField = new StringField("path", name, Field.Store.YES);
        doc.add(pathField);
        try {
            iw.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void idxClean() {
        try {
            System.out.println("clean all index");
            iw.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ok");
        }
    }

    private boolean readAndBuild() {
        if (!setIndexDir(indexDir))
            return false;
        idxClean();
        File dir = new File(baseDir);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] flist = dir.listFiles();
                assert flist != null;
                for (File f : flist) {
                    idxBuild(f);
                    // break; // TODO remove
                }
            } else {
                idxBuild(dir);
            }
        }
        this.close();
        return true;
    }

    public static void main(String[] args) {
        new IndexBuilder();
    }
}
