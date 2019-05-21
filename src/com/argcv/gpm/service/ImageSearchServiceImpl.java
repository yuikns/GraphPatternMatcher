package com.argcv.gpm.service;

import com.argcv.gpm.entity.ImageInfo;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchServiceImpl implements ImageSearchService {
    private File indexDir = null;

    public ImageSearchServiceImpl() {
        // indexDir = new File(ConfigService.get("index.indexDir"));
        indexDir = new File(ImageSearchServiceImpl.class.getResource(
                ConfigService.get("index.indexDir")).getPath());
    }

    public ImageSearchServiceImpl(String indexDirStr) {
        indexDir = new File(indexDirStr);
    }

    @Override
    public List<String> query(InputStream inputStream, int maxNum) {
        List<String> resultList = new ArrayList<String>();

        IndexSearcher searcher = null;
        try {
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory
                    .open(indexDir)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        searcher.setSimilarity(new BM25Similarity());

        ImageInfo ii = new ImageInfo(inputStream);
        BooleanQuery query = new BooleanQuery();

        Integer[][][] hslMatrix = ii.getHslmtx();
        if (!ii.isSuccess())
            return resultList;
        byte[] otsu = ii.getOtsumtx();
        for (int w = 0; w < ImageInfo.spSliceNum; w++) {
            for (int h = 0; h < ImageInfo.spSliceNum; h++) {
                String otsuk = String.format("OTSU_%d_%d", w, h);
                TermQuery otsutq = new TermQuery(new Term(otsuk,
                        String.valueOf(otsu[w * ImageInfo.spSliceNum + h])));
                otsutq.setBoost(6);
                query.add(new BooleanClause(otsutq, Occur.SHOULD));
                // HSL
                for (int z = 0; z < 3; z++) {
                    String k = String.format("HSL_%d_%d_%d", w, h, z);
                    TermQuery tq = new TermQuery(new Term(k,
                            String.valueOf(hslMatrix[w][h][z])));
                    switch (z) {
                        case 0:
                            tq.setBoost(8);
                            break;
                        case 1:
                            tq.setBoost(3);
                            break;
                        case 2:
                            tq.setBoost(3);
                            break;
                    }
                    query.add(new BooleanClause(tq, Occur.SHOULD));
                }
            }
        }
        Double[][][] colorHistogram = ii.getColorHistogram();
        for (int r = 0; r < 4; r++) {
            for (int g = 0; g < 4; g++) {
                for (int b = 0; b < 4; b++) {
                    String k = String
                            .format("ColorHistogram_%d_%d_%d", r, g, b);
                    // Field imgFeature = new DoubleField(k,
                    // colorHistogram[r][g][b], Field.Store.YES);
                    Query tq = NumericRangeQuery.newDoubleRange(k, colorHistogram[r][g][b] * 0.7, colorHistogram[r][g][b] * 1.3, true, true);
                    tq.setBoost(6);
                    query.add(tq, Occur.SHOULD);
                    // doc.add(imgFeature);
                    // System.out.println(String.format("ADD : %s : %f",k,colorHistogram[r][g][b]));
                }
            }
        }
        TopDocs td = null;
        try {
            td = searcher.search(query, maxNum);
        } catch (IOException e) {
            System.out.println("QUERY ERROR !@query or IO Exception");
            e.printStackTrace();
            return null;
        }

        ScoreDoc[] scs = td.scoreDocs;
        for (ScoreDoc sc : scs) {
            try {
                int docId = sc.doc;
                Document doc = searcher.doc(docId);
                resultList.add(doc.get("path"));

//				System.out.println("explain : " + searcher.explain(query, docId));
//				try {
//					Thread.sleep(1000000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    public static void main(String[] args) {
        // ImageSearchService iss = new ImageSearchServiceImpl(
        // ConfigService.get("index.indexDir"));
        ImageSearchService iss = new ImageSearchServiceImpl();
        // u=976853599,447330296&fm=21&gp=0.jpg
        // List<String> paths = iss.query(
        // "WebRoot/image/u=492679599,3528448622&fm=21&gp=0.jpg", 10);
        // File f = new
        // File("WebRoot/image/u=976853599,447330296&fm=21&gp=0.jpg");
        InputStream is = null;
        try {
            is = new FileInputStream(
                    "WebRoot/image/u=976853599,447330296&fm=21&gp=0.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> paths = iss.query(is, 10);
        for (String p : paths) {
            System.out.println(p);
        }
    }
}
