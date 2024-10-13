package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.DocumentEntity;
import com.example.proyecto.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@CrossOrigin("*")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<DocumentEntity>> listCreditDocuments(@PathVariable Long id){
        List<DocumentEntity> documents = documentService.getCreditDocuments(id);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> listDocuments(){
        List<DocumentEntity> documents = documentService.getAll();
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("creditID") Long creditID){
        try{
            String filename = file.getOriginalFilename();
            byte[] fileData = file.getBytes();

            DocumentEntity document = new DocumentEntity();
            document.setFilename(filename);
            document.setFileData(fileData);
            document.setCreditID(creditID);

            documentService.saveFile(document);

            return ResponseEntity.ok("File uploaded successfully");

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

}
