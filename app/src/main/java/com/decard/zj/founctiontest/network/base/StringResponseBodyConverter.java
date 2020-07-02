package com.decard.zj.founctiontest.network.base;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

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
