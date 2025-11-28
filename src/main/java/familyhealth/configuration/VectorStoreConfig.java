package familyhealth.configuration;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class VectorStoreConfig {
    
    private static final String VECTOR_STORE_FILE = "vector-store.json";
    private SimpleVectorStore simpleVectorStore;
    
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        simpleVectorStore = new SimpleVectorStore(embeddingModel);
        
        // Load existing vector store if available
        File vectorStoreFile = Paths.get(VECTOR_STORE_FILE).toFile();
        if (vectorStoreFile.exists()) {
            try {
                simpleVectorStore.load(vectorStoreFile);
                log.info("Loaded vector store from file: {}", VECTOR_STORE_FILE);
            } catch (Exception e) {
                log.warn("Could not load vector store: {}", e.getMessage());
            }
        } else {
            log.info("Vector store file not found, will create new one");
        }
        
        return simpleVectorStore;
    }
    
    @PreDestroy
    public void saveVectorStore() {
        if (simpleVectorStore != null) {
            try {
                File vectorStoreFile = Paths.get(VECTOR_STORE_FILE).toFile();
                simpleVectorStore.save(vectorStoreFile);
                log.info("Saved vector store to file: {}", VECTOR_STORE_FILE);
            } catch (Exception e) {
                log.error("Could not save vector store: {}", e.getMessage());
            }
        }
    }
}
