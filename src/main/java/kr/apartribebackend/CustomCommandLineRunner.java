package kr.apartribebackend;

import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("ApplicationInit Called() -2-");
        Category category1 = Category.builder().name("카테고리1").build();
        Category category2 = Category.builder().name("카테고리2").build();
        categoryRepository.saveAll(List.of(category1, category2));
    }
    
}

