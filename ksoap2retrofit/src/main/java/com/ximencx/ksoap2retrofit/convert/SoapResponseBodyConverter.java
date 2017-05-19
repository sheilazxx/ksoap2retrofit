package com.ximencx.ksoap2retrofit.convert;

import android.util.Log;

import  com.ximencx.ksoap2retrofit.ksoap2.SoapEnvelope;
import  com.ximencx.ksoap2retrofit.ksoap2.serialization.SoapObject;
import  com.ximencx.ksoap2retrofit.ksoap2.transport.SoapHelper;

import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Copyright (c) 2016. 东华博育云有限公司 Inc. All rights reserved.
 * com.mr_sun.logindemo.convert
 * 作者：Created by sfq on 2016/12/2.
 * http://www.jianshu.com/p/6fc8c9081c64
 * 联系方式：
 * 功能描述：
 * 修改：无
 */
public final class SoapResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "SoapResponseBodyConvert";

    private Class<?> clazz;

    protected SoapResponseBodyConverter(Type clazz) {
        this.clazz = (Class<?>) clazz;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //value 参考soap的返回，截取其中的字符串json
        SoapEnvelope soapEnvelope = SoapHelper.getInstance().getSoapEnvelope();
        SoapHelper.getInstance().parseResponse(soapEnvelope, value.byteStream());
        SoapObject resultsRequestSOAP = (SoapObject) soapEnvelope.bodyIn;
        Object obj = (Object) resultsRequestSOAP.getProperty(0);
        Log.e(TAG, "ResponseBodyToStringConverterFactory : " + obj.toString());
        try {
            //return adapter.fromJson(obj.toString());
            //return (T) obj.toString();
            return (T) new Persister().read(clazz, obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            value.close();
        }
    }
}