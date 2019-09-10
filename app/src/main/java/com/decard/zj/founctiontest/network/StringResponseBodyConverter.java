package com.decard.zj.founctiontest.network;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * File Description
 *
 * @author Dell
 * @date 2018/11/1
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
