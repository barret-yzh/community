package com.nowcoder.community.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * ElasticSearch配置类
 */

@Configuration
public class ElasticSearchClientConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //绑定主机，端口，协议，如果是ES集群，就配置多个
                        new HttpHost("192.168.137.100", 9200, "http")));
        return client;
    }
}
