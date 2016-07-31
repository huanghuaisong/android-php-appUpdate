package com.example.administrator.dongzhiwuapp.UpdateVersion;

/*
*
*  版本更新信息类
*  1、获取版本字符串信息
*  2、设置版本字符串信息
*  3、获取版本描述
*  4、设置版本描述
*  5、获取URL地址
*  6、设置URL地址
* */

public class UpdateInfo
{
        private String version;
        private String description;
        private String url;
        
        public String getVersion()
        {
                return version;
        }
        public void setVersion(String version)
        {
                this.version = version;
        }
        public String getDescription()
        {
                return description;
        }
        public void setDescription(String description)
        {
                this.description = description;
        }
        public String getUrl()
        {
                return url;
        }
        public void setUrl(String url)
        {
                this.url = url;
        }
        
}
