package com.argcv.gpm.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * base action for
 *
 * @author Yu
 */
public class BaseAction extends ActionSupport implements Preparable {
    /**
     *
     */
    private static final long serialVersionUID = 7699683171440127839L;
    public static final String DYNAMIC = "dynamic";
    public static final String REDIRECT = "redirect";

    private static String basePath;

    /**
     * return dynamic url
     */
    private String dynamicUrl;
    /**
     * return redirectUrl
     */
    private String redirectUrl;

    public void prepare() throws Exception {
        // 记录访问历史
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        Cookie cookie = new Cookie("lastUrl", request.getRequestURL()
                .toString());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 设置ajax返回的头部
     */
    public void setHeader() {
        HttpServletResponse response = getResponse();
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
    }

    public void setUser(String username) {
        getSessionMap().put("userName", username);
    }

    public void removeUser() {
        getSessionMap().remove("userName");
    }

    public String getUser() {
        return getSessionMap().get("userName").toString();
    }

    public void setUid(int uid) {
        getSessionMap().put("uid", String.valueOf(uid));
    }

    public int getUid() {
        return Integer.parseInt(getSessionMap().get("uid").toString());
    }

    private StringBuffer buildErrorStackTrace(String message, Exception e) {
        StringBuffer sb = new StringBuffer(message);
        sb.append("<br><b>").append(e.getClass().getName()).append(":").append(e.getMessage()).append("</b>");
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("<br>").append(stackTraceElement);
        }
        return sb;
    }

    protected StringBuffer showErrorMessage(String message, Exception e) {
        StringBuffer sb = buildErrorStackTrace(message, e);
        addActionError(sb.toString());
        return sb;
    }

    protected String showErrorMessage(String message) {
        addActionError(message);
        return message;
    }

    public ActionMapping getActionMapping() {
        return ServletActionContext.getActionMapping();
    }

    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public ActionContext getActionContext() {
        return ServletActionContext.getContext();
    }

    public Map<?, ?> getRequestMap() {
        return ServletActionContext.getContext().getParameters();
    }

    public Map<String, Object> getSessionMap() {
        return ServletActionContext.getContext().getSession();
    }

    public ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    public String getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(String dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public static String getBasePath() {
        return basePath;
    }

    public static void setBasePath(String basePath) {
        BaseAction.basePath = basePath;
    }

    protected boolean setStringToReturn(String str) {
        getResponse().setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = getResponse().getWriter();
            pw.write(str);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pw != null)
                pw.close();
        }
        return true;
    }

}
