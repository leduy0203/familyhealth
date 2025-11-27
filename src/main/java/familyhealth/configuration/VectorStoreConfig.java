package familyhealth.configuration;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class VectorStoreConfig {
    
    private static final String VECTOR_STORE_FILE = "vector-store.json";
    
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        
        // Load existing vector store if available
        File vectorStoreFile = Paths.get(VECTOR_STORE_FILE).toFile();
        if (vectorStoreFile.exists()) {
            vectorStore.load(vectorStoreFile);
        }
        
        return vectorStore;
    }
}
