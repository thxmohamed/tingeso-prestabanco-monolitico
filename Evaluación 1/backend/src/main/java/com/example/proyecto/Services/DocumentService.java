package com.example.proyecto.Services;

import com.example.proyecto.Entities.DocumentEntity;
import com.example.proyecto.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public List<DocumentEntity> getCreditDocuments(Long id){
        return documentRepository.findByCreditID(id);
    }

    public List<DocumentEntity> getAll(){
        return documentRepository.findAll();
    }
    public DocumentEntity saveFile(DocumentEntity documentEntity){
        return documentRepository.save(documentEntity);
    }


}
