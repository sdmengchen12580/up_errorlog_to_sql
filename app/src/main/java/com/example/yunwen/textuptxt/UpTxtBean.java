package com.example.yunwen.textuptxt;

import java.util.List;

/**
 * Created by yunwen on 2017/9/22.
 */

public class UpTxtBean {


    /**
     * files : [{"deleteType":"DELETE","deleteUrl":"http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls","error":"","name":"模板_问题导入20170918171546.xls","size":5220,"thumbnailUrl":"http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls","url":"http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls"}]
     * materialId : 105
     */

    private int materialId;
    private List<FilesBean> files;

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        /**
         * deleteType : DELETE
         * deleteUrl : http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls
         * error :
         * name : 模板_问题导入20170918171546.xls
         * size : 5220
         * thumbnailUrl : http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls
         * url : http://v4.faqrobot.org/upload/web/1476067342641247/20170922/68731506047488264.xls
         */

        private String deleteType;
        private String deleteUrl;
        private String error;
        private String name;
        private int size;
        private String thumbnailUrl;
        private String url;

        public String getDeleteType() {
            return deleteType;
        }

        public void setDeleteType(String deleteType) {
            this.deleteType = deleteType;
        }

        public String getDeleteUrl() {
            return deleteUrl;
        }

        public void setDeleteUrl(String deleteUrl) {
            this.deleteUrl = deleteUrl;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
