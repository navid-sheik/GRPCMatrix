package com.example.grpc.client.grpcclient;

import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;
import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
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
import java.util.*;

@Service
public class GRPCClientService {
	public String ping() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
				.usePlaintext()
				.build();
		PingPongServiceGrpc.PingPongServiceBlockingStub stub = PingPongServiceGrpc.newBlockingStub(channel);
		PongResponse helloResponse = stub.ping(PingRequest.newBuilder()
				.setPing("")
				.build());
		channel.shutdown();
		return helloResponse.getPong();
	}

	// public String add() {
	// 	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
	// 			.usePlaintext()
	// 			.build();
	// 	MatrixServiceGrpc.MatrixServiceBlockingStub stub = MatrixServiceGrpc.newBlockingStub(channel);
	// 	MatrixReply A = stub.addBlock(MatrixRequest.newBuilder()
	// 			.setA00(1)
	// 			.setA01(2)
	// 			.setA10(5)
	// 			.setA11(6)
	// 			.setB00(2)
	// 			.setB01(3)
	// 			.setB10(6)
	// 			.setB11(7)
	// 			.build());
	// 	String resp = A.getC00() + A.getC01() + A.getC10() + A.getC11() + "";
	// 	return resp;
	// }

	// public String multMatrix() {
	// 	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
	// 			.usePlaintext()
	// 			.build();
	// 	MatrixServiceGrpc.MatrixServiceBlockingStub stub = MatrixServiceGrpc.newBlockingStub(channel);
	// 	MatrixReply A = stub.multiplyBlock(MatrixRequest.newBuilder()
	// 			.setA(1)
	// 			.setB(2)
	// 			.build());
	// 	String resp = A.getA() * A.getB() "";
	// 	return resp;

	// }

	public String fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2)
			throws IOException {
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

		// if (!checkMatrix(matrixA, matrixB))
		// 	System.out.println("Somethign wrong , adjust your matrix");

		// clientOperation(matrixA, matrixB);

		return firstMatrix + secondMatrix + "hello";

	}

	private static  void clientOperation(int[][] matrixA, int[][] matrixB){
		//perform multiplication
		//Source https://www.javatpoint.com/java-program-to-multiply-two-matrices
		
		//new size matrix  
		int[][] matrixC =  new int[matrixA.length][matrixB.length];


		//multiplying and printing multiplication of 2 matrices    
		for(int i=0;i<matrixA.length;i++){    
			for(int j=0;j<matrixB.length;j++){    
				matrixC[i][j]=0;      
				for(int k=0;k<matrixA.length;k++)      
				{  
					
					//multiple first row A with first colum B, and so on 
					//Calculate the first mutiplication and store in C[i][j], add the result as we multiple the other values

				}
			}
		}
	}

	private static int[][] convertMatrixToString(String matrixString) {
		// Split the matrix based on the space
		String[] str1 = matrixString.split(" ");
		System.out.println(Arrays.toString(str1));
		int[][] matrix = new int[str1.length][];
		for (int i = 0; i < matrix.length; i++) {
			String[] str2 = str1[i].split(",");
			matrix[i] = new int[str2.length];
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = Integer.parseInt(str2[j]);
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
}
