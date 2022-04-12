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
    //     @GetMapping("/add")
	// public String add() {
	// 	return grpcClientService.add();
	// }
	// @GetMapping("/mult")
    //     public String mult() {
    //             return grpcClientService.multMatrix();
    //     }
	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2)throws IOException  {
		String firstMatrix = new String(file.getBytes(), StandardCharsets.UTF_8);
		System.out.println("File 1 uploaded correctlu");
		System.out.println(firstMatrix);

		String secondMatrix = new String(file2.getBytes(), StandardCharsets.UTF_8);
		System.out.println("File 2 uploaded correctlu");
		System.out.println(secondMatrix);

		int[][] matrixA = convertMatrixToString(firstMatrix);
		int[][] matrixB = convertMatrixToString(secondMatrix);
		System.out.println(Arrays.deepToString(matrixA));
		System.out.println(Arrays.deepToString(matrixB));

		if (!checkMatrix(matrixA, matrixB)){
			System.out.println("Somethign wrong , adjust your matrix");
			return "Not successful";
		}
		
		this.grpcClientService.clientOperation(matrixA, matrixB);

		return "successful" ;


		
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


	private static int[][] convertMatrixToString(String matrixString) {
		// Split the matrix based on the space
		String pattern = Pattern.quote("\\" + "n");
		String[] str1 = matrixString.split("\\|");
		int[][] matrix = new int[str1.length][];
		for (int i = 0; i < matrix.length; i++) {
			String[] str2 = str1[i].split(",");
			System.out.println(Arrays.toString(str2));
			matrix[i] = new int[str2.length];
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = Integer.parseInt(str2[j].trim());
			}
		}
		return matrix;
	}

	private static boolean checkMatrix(int[][] matrixA, int[][] matrixB) {
		return (isSquareMatrix(matrixA) && isMatrixPowerOfTwo(matrixA))
				&& (isSquareMatrix(matrixB) && isMatrixPowerOfTwo(matrixB));
	}

	private static boolean isPowerOfTwo(int n) {
		return (int) (Math.ceil((Math.log(n) / Math.log(2)))) == (int) (Math.floor(((Math.log(n) / Math.log(2)))));
	}

	private static boolean isMatrixPowerOfTwo(int[][] matrix) {

		int rowNumber = matrix.length;
		if (!isPowerOfTwo(rowNumber))
			return false;
		for (int i = 0; i < rowNumber; i++) {
			if (!isPowerOfTwo(matrix[i].length)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSquareMatrix(int[][] matrix) {
		int rowNumber = matrix.length;
		for (int i = 0; i < rowNumber; i++) {
			if (matrix[i].length != rowNumber) {
				return false;
			}
		}
		return true;
	}
	//Source :https://www.geeksforgeeks.org/print-2-d-array-matrix-java/
	public static void print2D(int mat[][])
    {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++)
 
            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
    }



	



}
