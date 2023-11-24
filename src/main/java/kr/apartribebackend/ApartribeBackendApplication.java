package kr.apartribebackend;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.TimeZone;


@Slf4j
@SpringBootApplication
public class ApartribeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartribeBackendApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	@EventListener(value = ApplicationReadyEvent.class)
	public void eventListener() {
		log.info("ApplicationReadyEvent called() -3-");
	}

}
