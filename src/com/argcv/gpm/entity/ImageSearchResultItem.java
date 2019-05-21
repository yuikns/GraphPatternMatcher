package com.argcv.gpm.entity;

import java.util.ArrayList;
import java.util.List;

public class ImageSearchResultItem {
    public class Status {
        String query;
        double cost_time;
        int all_result;
        int all_page;
        int current_page;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public double getCost_time() {
            return cost_time;
        }

        public void setCost_time(double cost_time) {
            this.cost_time = cost_time;
        }

        public int getAll_result() {
            return all_result;
        }

        public void setAll_result(int all_result) {
            this.all_result = all_result;
        }

        public int getAll_page() {
            return all_page;
        }

        public void setAll_page(int all_page) {
            this.all_page = all_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }
    }

    public class Result {
        String title;
        String link;
        String time;
        String abs;
        String image;

        public Result() {

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAbstract() {
            return abs;
        }

        public void setAbstract(String abs) {
            this.abs = abs;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    Status status;
    List<Result> result;

    public Status getStatus() {
        if (status == null) status = new Status();
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Result> getResult() {
        if (result == null) result = new ArrayList<Result>();
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public Result newResult() {
        return new Result();
    }
}
