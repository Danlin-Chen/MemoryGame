package iss.workshop.memorygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageDownload {


    protected String getUrlString(String inputUrl) {
        try {
            URL url = new URL(inputUrl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
            } else {
                System.out.println("Please enter URL again");//need edit
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String urlString = "";
            String current;
            while ((current = in.readLine()) != null) {
                urlString += current;
            }
            return urlString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<String> imgUrlList(String urlString){
        List<String> srcList = new ArrayList<String>(); //用来存储获取到的图片地址
        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");//匹配字符串中的img标签
        Matcher matcher = p.matcher(urlString);
        boolean hasPic = matcher.find();
        if(hasPic)//判断是否含有图片
        {
            while (hasPic)
            {
                String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");//匹配图片的地址
                Matcher matcher2 = srcText.matcher(group);
                if (matcher2.find()) {
                    srcList.add(matcher2.group(3));//把获取到的图片地址添加到列表中
                }
                hasPic = matcher.find();
            }
        }

        Iterator<String> iterator=srcList.iterator();
        while (iterator.hasNext()){
            String url=iterator.next();
            if(!url.contains("jpg")){
                iterator.remove();
            }
        }

        return srcList;
    }

    protected Bitmap downloadImage (String imgURL){
        try{
            URL url=new URL(imgURL);
            URLConnection conn=url.openConnection();
//            conn.setDoInput(true);
//            conn.setConnectTimeout(10*1000);
//            conn.connect();

            InputStream in=conn.getInputStream();
            Bitmap bitmap= BitmapFactory.decodeStream(in);

            in.close();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
