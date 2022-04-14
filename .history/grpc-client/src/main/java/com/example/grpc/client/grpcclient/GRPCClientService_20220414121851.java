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

import ch.qos.logback.core.joran.conditional.ElseAction;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

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

	// public String fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2)
	// 		throws IOException {
		
	// }

	public  void clientOperation(int[][] matrixA, int[][] matrixB, int deadline){
		//perform multiplication
		
		
		//new size matrix  
		int[][] matrixC =  new int[matrixA.length][matrixB.length];
		
		//Channels 8
		ManagedChannel channel1 = ManagedChannelBuilder.forAddress("34.72.177.37", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel2 = ManagedChannelBuilder.forAddress("34.122.127.20", 9090)
								.usePlaintext()
								.build();
		ManagedChannel channel3 = ManagedChannelBuilder.forAddress("34.72.101.175", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel4 = ManagedChannelBuilder.forAddress("34.69.225.39", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel5 = ManagedChannelBuilder.forAddress("35.238.52.183", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel6 = ManagedChannelBuilder.forAddress("34.121.219.175", 9090)
								.usePlaintext()
								.build();
		ManagedChannel channel7 = ManagedChannelBuilder.forAddress("35.223.57.119", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel8 = ManagedChannelBuilder.forAddress("34.67.241.158", 9090)
								.usePlaintext()
								.build();
						

		//8 Stubs 
		MatrixServiceGrpc.MatrixServiceBlockingStub stub1 = MatrixServiceGrpc.newBlockingStub(channel1);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub2 = MatrixServiceGrpc.newBlockingStub(channel2);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub3 = MatrixServiceGrpc.newBlockingStub(channel3);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub4 = MatrixServiceGrpc.newBlockingStub(channel4);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub5= MatrixServiceGrpc.newBlockingStub(channel5);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub6 = MatrixServiceGrpc.newBlockingStub(channel6);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub7 = MatrixServiceGrpc.newBlockingStub(channel7);
		MatrixServiceGrpc.MatrixServiceBlockingStub stub8 = MatrixServiceGrpc.newBlockingStub(channel8);
		List<MatrixServiceGrpc.MatrixServiceBlockingStub> listOfStubs  =  new ArrayList<MatrixServiceGrpc.MatrixServiceBlockingStub>(Arrays.asList(stub1,stub2,stub3,stub4,stub5,stub6,stub7,stub8) );
	
		//multiplying and printing multiplication of 2 matrices  
		
		//127 seconds deadline
		long footprint =  calculateFootprint(stub1);
		int numberOfCalls =  calculateNumberOfCalls(matrixA.length);
		int server_needed  =  calculateServersRequired(numberOfCalls, footprint, deadline);
		System.out.println("The server being used" + server_needed); 
		
		//Source https://www.javatpoint.com/java-program-to-multiply-two-matrices
		int stubInUse   = 0;
		for(int rowA=0;rowA<matrixA.length;rowA++){    
			for(int rowB=0;rowB<matrixB.length;rowB++){    
				matrixC[rowA][rowB]=0;     
				//Do it for all rows/columns  - only works with square matrix 
				for(int k=0;k<matrixA.length;k++)      
				{  
					stubInUse =  stubInUse == server_needed-1 ?  0 : stubInUse++;

					//Calculate the multiplication  between two values 
					MatrixReply  multValue = listOfStubs.get(stubInUse).multiplyBlock(MatrixRequest.newBuilder()
												.setA(matrixA[rowA][k])
												.setB(matrixB[k][rowB])
												.build());
					stubInUse =  stubInUse == server_needed-1?  0 : stubInUse++;

					//Add the value to existing c[i][j] - initiallly 0
					MatrixReply  addValue = listOfStubs.get(stubInUse).addBlock(MatrixRequest.newBuilder()
												.setA(multValue.getC())
												.setB(matrixC[rowA][rowB])
												.build());
				
					//Update the value
					matrixC[rowA][rowB] = addValue.getC();
					stubInUse =  stubInUse == server_needed-1 ?  0 : stubInUse++;

				}
			}
		}
		print2D(matrixC);
		channel1.shutdown();
		channel2.shutdown();
		channel3.shutdown();
		channel3.shutdown();
		channel4.shutdown();
		channel5.shutdown();
		channel6.shutdown();
		channel7.shutdown();
		channel8.shutdown();
	


	
	}
	

	
	private long calculateFootprint(MatrixServiceGrpc.MatrixServiceBlockingStub  stub){
		long startTime =  System.currentTimeMillis();
		
		MatrixReply  test = stub.multiplyBlock(MatrixRequest.newBuilder()
												.setA(4)
												.setB(5)
												.build());
		long endTime =  System.currentTimeMillis();
		long footprint= endTime-startTime;
		//convert to seconds 
		System.out.println("The footprint in millisecond " + footprint);
		return footprint/1000;

	}

	private int calculateNumberOfCalls(int matrixDimension){
		int elementsInMatrix  =  matrixDimension * matrixDimension;
		double numberOfCallsDouble  = elementsInMatrix * matrixDimension;
		int numberOfCallsInt  = (int) Math.round(numberOfCallsDouble);
		return  numberOfCallsInt;
	}

	private int calculateServersRequired(int numBlockCalls, long footprint, int deadline){
		System.out.println("The footprint is " + footprint);
		System.out.println("The number of  calls  is " + numBlockCalls);
		System.out.println("The deadline   is " +  deadline);
		//default deadline = 127 seconds
		System.out.print("footprint + numbblocks calls"  + (footprint*numBlockCalls));
		long numberServerLong=(footprint*numBlockCalls)/deadline;
		int  numberServer= (int) Math.round(numberServerLong);
		if (numberServer > 8)
			numberServer = 8;
		else if (numberServer < 1)
			numberServer = 1;
		return numberServer;


	}


	private static int[][] convertMatrixToString(String matrixString) {
		// Split the matrix based on the space
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
			System.out.println(" ");
    }
 
}
