package com.example.grpc.client.grpcclient;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.Collectors;
import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
@RestController
public class PingPongEndpoint {    
	 public static int matrixA = 0;
	public static int matrixB =  0 ;

	GRPCClientService grpcClientService;    
	@Autowired
    	public PingPongEndpoint(GRPCClientService grpcClientService) {
        	this.grpcClientService = grpcClientService;
    	}    
	@GetMapping("/ping")
    	public String ping() {
        	return grpcClientService.ping();
    	}
        @GetMapping("/add")
	public String add() {
		return grpcClientService.add();
	}
	@GetMapping("/mult")
        public String mult() {
                return grpcClientService.multMatrix();
        }
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2)throws IOException  {
		return grpcClientService.fileUpload(file, file2);

		
//		try{
//			String content = new String(file.getBytes());
//			System.out.println(content);
//		}catch(Expection e){
//			System.out.println("Error on uploading");
//		}
//		redirectAttributes.addFlashAttribute("message",
//				"You successfully uploaded " + file.getOriginalFilename() + "!");

//		return "uploaded";
	}



	



}
