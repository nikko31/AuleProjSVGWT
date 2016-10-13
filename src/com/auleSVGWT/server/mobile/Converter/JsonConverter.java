package com.auleSVGWT.server.mobile.Converter;

public interface JsonConverter<Src,Dst> {

    public Dst convert(Src value);
}