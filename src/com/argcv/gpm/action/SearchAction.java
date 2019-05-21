package com.argcv.gpm.action;

import com.argcv.gpm.entity.ImageSearchResultItem;
import com.argcv.gpm.entity.ImageSearchResultItem.Result;
import com.argcv.gpm.service.ImageSearchService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * main process for search request this.setDynamicUrl("/xxx.jsp"); return
 * DYNAMIC;
 *
 * @author Yu
 */
public class SearchAction extends BaseAction {
    private static final long serialVersionUID = -4393331357342789810L;
    private ImageSearchService imageSearchService;

    // private List<MultipartFile> imgtosrch;
    /*
     * public void searchImageUpload() throws Exception { Gson g = new Gson();
     * ImageSearchResultItem isri = new ImageSearchResultItem();
     * ImageSearchResultItem.Status status = isri.getStatus(); try {
     * System.out.println("received !"); long costTimeStart =
     * System.currentTimeMillis(); MultiValueMap<String, MultipartFile> fileMap
     * = ((MultipartHttpServletRequest)getRequest()).getMultiFileMap();
     * List<MultipartFile> imgtosrch = fileMap.get("imgtosrch"); if (imgtosrch
     * == null || imgtosrch.size() < 1) { String tip = "no file found !" +
     * (imgtosrch == null ? "NULL" : "EMPTY"); System.err.println(tip); throw
     * new Exception(tip); } // MultipartFile mf = imgtosrch.get(0);
     * List<String> rst = imageSearchService.query(imgtosrch.get(0)
     * .getInputStream(), 1024); long costTimeEnd = System.currentTimeMillis();
     * // imageSearchService.query(queryFilePath, maxNum);
     *
     * status.setAll_result(rst.size()); status.setCost_time(costTimeEnd -
     * costTimeStart); System.out.println("result :"); for (String s : rst) {
     * System.out.println("::" + s); }
     *
     * List<Result> rs = isri.getResult(); for (String s : rst) {
     * ImageSearchResultItem.Result r = isri.newResult(); r.setImage("image/" +
     * s); rs.add(r); } System.out.println(g.toJson(isri));
     * setStringToReturn(g.toJson(isri)); } catch (Exception e) {
     * e.printStackTrace(); setStringToReturn(g.toJson(isri)); } }
     */
    private File imgtosrch;
    private String imgtosrchContentType;
    private String imgtosrchFileName;

    public void searchImageUpload() throws Exception {
        Gson g = new Gson();
        ImageSearchResultItem isri = new ImageSearchResultItem();
        ImageSearchResultItem.Status status = isri.getStatus();
        try {
            if (imgtosrch == null) {
                System.err.println("ERROR : imgtosrch is null !!!!");
            } else {
                System.out.println("imgtosrch is found !!");
            }
            long costTimeStart = System.currentTimeMillis();
            // MultipartFile mf = imgtosrch.get(0);
            FileInputStream fis = new FileInputStream(getImgtosrch());
//			System.out.println("dest file:" + imgtosrch.getAbsolutePath() + "/"
//					+ imgtosrchFileName);
            List<String> rst = imageSearchService.query(fis, 10);
            fis.close();
            if (rst == null) {
                System.err.println("RETURN NULL ????!!!!!");
            } else {
                System.err.println("RETURN NOT NULL !!!!!");
            }
            long costTimeEnd = System.currentTimeMillis();
            // imageSearchService.query(queryFilePath, maxNum);
            System.out.println("all result number :" + rst.size());
            status.setAll_result(rst.size());
            status.setCost_time((double) (costTimeEnd - costTimeStart) / 1000);
            System.out.println("result :");
            for (String s : rst) {
                System.out.println("::" + s);
            }

            List<Result> rs = isri.getResult();
            for (String s : rst) {
                ImageSearchResultItem.Result r = isri.newResult();
                r.setImage("image/" + s);
                rs.add(r);
            }
            System.out.println(g.toJson(isri));
            setStringToReturn(g.toJson(isri));
            System.out.println("ALL FINE ?");
        } catch (Exception e) {
            System.err.println("ERR FROM SearchAction!!");
            e.printStackTrace();
            setStringToReturn(g.toJson(isri));
        }
    }

    // servletRequest.getSession().getServletContext().getRealPath(“/”);

    public ImageSearchService getImageSearchService() {
        return imageSearchService;
    }

    public void setImageSearchService(ImageSearchService imageSearchService) {
        this.imageSearchService = imageSearchService;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public File getImgtosrch() {
        return imgtosrch;
    }

    public void setImgtosrch(File imgtosrch) {
        this.imgtosrch = imgtosrch;
    }


    public String getImgtosrchContentType() {
        return imgtosrchContentType;
    }

    public void setImgtosrchContentType(String imgtosrchContentType) {
        this.imgtosrchContentType = imgtosrchContentType;
    }

    public String getImgtosrchFileName() {
        return imgtosrchFileName;
    }

    public void setImgtosrchFileName(String imgtosrchFileName) {
        this.imgtosrchFileName = imgtosrchFileName;
    }

}
