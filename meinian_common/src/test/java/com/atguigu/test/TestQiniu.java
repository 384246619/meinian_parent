package com.atguigu.test;

import com.atguigu.util.QiniuUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestQiniu {
    // 上传本地文件
    @Test
    public void uploadFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());//华南
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "MpSvSDFmn0yewoU5lWihumpzKnh2f5NAJbAHFmC5";
        String secretKey = "kwUVKNHJhG124bYp9ayTY0k9EVlr_CfexEtvST3G";
        String bucket = "meinian20220716";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png，可支持中文
        String localFilePath = "C:\\Users\\38424\\Pictures\\Camera Roll\\window.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);// FspfyEyKfuHZ0kcbXRIc5T9YhCax
            System.out.println(putRet.hash);// FspfyEyKfuHZ0kcbXRIc5T9YhCax
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    // 删除空间中的文件
    @Test
    public void deleteFile(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "MpSvSDFmn0yewoU5lWihumpzKnh2f5NAJbAHFmC5";
        String secretKey = "kwUVKNHJhG124bYp9ayTY0k9EVlr_CfexEtvST3G";
        String bucket = "meinian20220716";
        String key = "Flk4jdL5mQ6ZyfLtT-q419HpVjKm";//文件名称
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

//    @Test
//    public void test() throws IOException {
//        //QiniuUtils.upload2Qiniu("D:\\temp\\90\\jjy94.jpg","jjy94.jpg");
//
//        //QiniuUtils.deleteFileFromQiniu("jjy94.jpg");
//
//        File file = new File("D:\\temp\\90\\jjy94.jpg");
//        //init array with file length
//        byte[] bytesArray = new byte[(int) file.length()];
//        FileInputStream fis = new FileInputStream(file);
//        fis.read(bytesArray); //read file into bytes[]
//        QiniuUtils.upload2Qiniu(bytesArray,"jjy94.jpg");
//    }
}
