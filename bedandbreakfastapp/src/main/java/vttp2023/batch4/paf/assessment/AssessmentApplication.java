package vttp2023.batch4.paf.assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp2023.batch4.paf.assessment.repositories.BookingsRepository;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner {
	
	@Autowired
	private ListingsRepository lrepo;

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) {
		lrepo.getSuburbs("australia");
	}
}
