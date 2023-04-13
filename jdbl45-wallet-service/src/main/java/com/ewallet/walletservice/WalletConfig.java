package com.ewallet.walletservice;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class WalletConfig {

	Properties getProducerConfig()
	 {
		 Properties prop=new Properties();
		 
		 prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		 prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
		 prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);

		 return prop;
	 }
	 
	 ProducerFactory<String,String> getProducerFactory()
	 {
		 return new DefaultKafkaProducerFactory(getProducerConfig());
	 }
	 
	 @Bean
	 KafkaTemplate<String,String> getKafkaTemplate()
	 {
		 return new KafkaTemplate<>(getProducerFactory());
	 }
	 
	 //ConsumerFactory
	
	 Properties getConsumerConfigs(){
	        Properties properties = new Properties();
	        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        return properties;
	    }

	    @Bean
	    ConsumerFactory<String, String> getConsumerFactory(){
	        return new DefaultKafkaConsumerFactory(getConsumerConfigs());
	    }
}
