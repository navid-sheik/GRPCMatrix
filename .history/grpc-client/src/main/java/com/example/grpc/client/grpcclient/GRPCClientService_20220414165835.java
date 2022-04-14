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
		ManagedChannel channel1 = ManagedChannelBuilder.forAddress("35.188.43.26", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel2 = ManagedChannelBuilder.forAddress("34.68.153.198", 9090)
								.usePlaintext()
								.build();
		ManagedChannel channel3 = ManagedChannelBuilder.forAddress("35.238.47.221", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel4 = ManagedChannelBuilder.forAddress("104.198.215.212", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel5 = ManagedChannelBuilder.forAddress("34.122.244.16", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel6 = ManagedChannelBuilder.forAddress("35.202.163.172", 9090)
								.usePlaintext()
								.build();
		ManagedChannel channel7 = ManagedChannelBuilder.forAddress("34.68.204.229", 9090)
								.usePlaintext()
								.build();

		ManagedChannel channel8 = ManagedChannelBuilder.forAddress("35.224.82.28", 9090)
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
		double footprint =  calculateFootprint(listOfStubs);
		int numberOfCalls =  calculateNumberOfCalls(matrixA.length);
		int server_needed  =  calculateServersRequired(numberOfCalls, footprint, deadline);
		System.out.println("The server required : " + server_needed); 
		
		//Source https://www.javatpoint.com/java-program-to-multiply-two-matrices
		int stubInUse   = 0;
		for(int rowA=0;rowA<matrixA.length;rowA++){    
			for(int rowB=0;rowB<matrixB.length;rowB++){    
				matrixC[rowA][rowB]=0;     
				//Do it for all rows/columns  - only works with square matriX
				for(int k=0;k<matrixA.length;k++)      
				{  
					stubInUse =  stubInUse == server_needed-1 ?  0 : stubInUse + 1;

					//Calculate the multiplication  between two values 
					MatrixReply  multValue = listOfStubs.get(stubInUse).multiplyBlock(MatrixRequest.newBuilder()
												.setA(matrixA[rowA][k])
												.setB(matrixB[k][rowB])
												.build());
					stubInUse =  stubInUse == server_needed-1?  0 : stubInUse + 1;

					//Add the value to existing c[i][j] - initiallly 0
					MatrixReply  addValue = listOfStubs.get(stubInUse).addBlock(MatrixRequest.newBuilder()
												.setA(multValue.getC())
												.setB(matrixC[rowA][rowB])
												.build());
				
					//Update the value
					matrixC[rowA][rowB] = addValue.getC();
					stubInUse =  stubInUse == server_needed-1 ?  0 : stubInUse + 1;

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
	public  int  randomNumber(int leftLimit, int rightLimit) {

		int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
		return generatedInteger;
	}

	
	private double calculateFootprint(List<MatrixServiceGrpc.MatrixServiceBlockingStub> stubs){
		long startTime =  System.currentTimeMillis();
		int randomStub  =  randomNumber(0, stubs.size() - 1);
		MatrixReply  test = stubs.get(randomStub).multiplyBlock(MatrixRequest.newBuilder()
												.setA(randomNumber(0, 8))
												.setB(randomNumber(0,8))
												.build());
		long endTime =  System.currentTimeMillis();
		long footprint= endTime-startTime;
		//convert to seconds 
		System.out.println("The footprint in millisecond " + footprint);
		double footprintDouble=  Double.valueOf(footprint);
		return footprintDouble/1000;

	}

	private int calculateNumberOfCalls(int matrixDimension){
		int elementsInMatrix  =  matrixDimension * matrixDimension;
		double numberOfCallsDouble  = elementsInMatrix * matrixDimension;
		int numberOfCallsInt  = (int) Math.round(numberOfCallsDouble);
		return  numberOfCallsInt;
	}

	private int calculateServersRequired(int numBlockCalls, double footprint, int deadline){
		System.out.println("The footprint in seconds is " + footprint);
		System.out.println("The number of  calls  are " + numBlockCalls);
		System.out.println("The deadline is " +  deadline);
		//default deadline = 127 seconds
		double numberServerLong=(footprint*numBlockCalls)/deadline;
		int  numberServer= (int) Math.round(numberServerLong);
		//If the number of server exceed the available servers
		if (numberServer > 8)
			numberServer = 8;
		//if the number of server required is less 1
		else if (numberServer < 1)
			numberServer = 1;
		return numberServer;


	}
	
	//Source :https://www.geeksforgeeks.org/print-2-d-array-matrix-java/
	public static void print2D(int mat[][])
    {
		System.out.println(Arrays.deepToString(mat).replace("], ", "]\n"));
    }
 
}
