package com.jd.jmi;

import com.jd.cachecloud.cfs.client.ConfigRESTClientFactory;
import com.jd.jim.cli.Cluster;
import com.jd.jim.cli.ReloadableJimClientFactory;
import com.jd.jim.cli.serializer.DefaultObjectSerializer;
import com.jd.jim.cli.serializer.DefaultStringSerializer;
import com.jd.jim.cli.serializer.Serializer;

import java.nio.charset.Charset;

/**
 * Created by wangzhen23 on 2016/11/25.
 */
public class JimClientDemo {
    private static class JimClientFactory {
        private static final Cluster CLIENT_INSTANCE;

        static {
            // 1.cfs客户端配置(大多数情况使用默认配置即可)
            // 一个cfs客户端只能由一个Jim客户端对象使用
            // 如果一个应用使用多个Jim客户端，则需要设置唯一的appcode，如：cfs.setAppCode("cart");
            // 如果是一个服务器部署多个实例的情况或者Hadoop、Storm、Spark环境，需要设置localSave属性为false，如:cfs.setLocalSave(false);
            ConfigRESTClientFactory cfsClient = new ConfigRESTClientFactory();
            cfsClient.setServiceEndpoint("http://cfs.id.jd.local");//印尼项目使用，非印尼项目不要添加这一行，印尼项目是香港机房

            // 2.Jim客户端配置
            ReloadableJimClientFactory clientFactory = new ReloadableJimClientFactory();
            clientFactory.setConfigClient(cfsClient.create());
            clientFactory.setConfigId("/redis/cluster/72");// 配置拥有集群的客户端配置ID(必选)
            clientFactory.setToken("1382523437163");// 配置拥有集群的客户端配置Token(必选)
            // 配置字符串序列化器,默认为'UTF-8'字节序列化(可选)
            Serializer<String> stringSerializer = new DefaultStringSerializer(Charset.forName("UTF-8"));
            clientFactory.setStringSerializer(stringSerializer);
            // 配置Object序列化器,默认为JDK序列化(可选)
            Serializer<Object> objSerializer = new DefaultObjectSerializer(16384);// 压缩阀值(默认16K)
            clientFactory.setObjectSerializer(objSerializer);

            CLIENT_INSTANCE = clientFactory.getClient();
        }

        public static Cluster getJimClient() {
            return CLIENT_INSTANCE;
        }
    }

    public String getByKey(String key) {
        return JimClientFactory.getJimClient().get(key);
    }
}
