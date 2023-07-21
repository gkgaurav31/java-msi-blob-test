package com.example.demo;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;

@RestController
public class HelloController {
    // Replace these values with your actual Azure Storage Account details
    private static final String STORAGE_ACCOUNT_NAME = "linuxgroup945a";
    private static final String CONTAINER_NAME = "myblob";

    @GetMapping("/test")
    public String hello() {
        try {
            // Create a Managed Identity Credential
            ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder().build();

            // Create a BlobContainerClient using Managed Identity for authentication
            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .endpoint("https://" + STORAGE_ACCOUNT_NAME + ".blob.core.windows.net")
                    .credential(credential)
                    .containerName(CONTAINER_NAME)
                    .buildClient();

            // Create a sample text file content
            String fileContent = "Hello, this is a test file created using Azure Blob Storage and Managed Identity!";

            // Get a reference to a blob in the container
            BlockBlobClient blobClient = blobContainerClient.getBlobClient("hello.txt").getBlockBlobClient();

            // Upload the text content to the blob
            blobClient.upload(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), fileContent.length());

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while creating the file.";
        }

        return "File 'hello.txt' has been created in the blob container.";
    }
}
