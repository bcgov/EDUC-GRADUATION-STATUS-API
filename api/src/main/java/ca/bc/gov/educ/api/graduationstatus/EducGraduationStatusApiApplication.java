package ca.bc.gov.educ.api.graduationstatus;

import ca.bc.gov.educ.api.graduationstatus.model.dto.GraduationData;
import ca.bc.gov.educ.api.graduationstatus.model.entity.GraduationStatusEntity;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EducGraduationStatusApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EducGraduationStatusApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(GraduationStatusEntity.class, GraduationData.class).addMappings(mapper -> {
			mapper.skip(GraduationData::setStatusDate);
		});

		modelMapper.typeMap(GraduationData.class, GraduationStatusEntity.class).addMappings(mapper -> {
			mapper.skip(GraduationStatusEntity::setUpdatedTimestamp);
		});

		return modelMapper;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
