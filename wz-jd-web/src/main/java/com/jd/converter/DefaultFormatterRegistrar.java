package com.jd.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class DefaultFormatterRegistrar implements FormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        if (registry instanceof ConversionService) {
            ConversionService service = (ConversionService) registry;
            registry.addConverterFactory(new StringToEnumConverterFactory(
                    service));
        }
        // register custom converter.
    }

}