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
@Service
public class GRPCClientService {
    public String ping() {
        	ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();        
		PingPongServiceGrpc.PingPongServiceBlockingStub stub
                = PingPongServiceGrpc.newBlockingStub(channel);        
		PongResponse helloResponse = stub.ping(PingRequest.newBuilder()
                .setPing("")
                .build());        
		channel.shutdown();        
		return helloResponse.getPong();
    }
    public String add(){
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
		.usePlaintext()
		.build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub
		 = MatrixServiceGrpc.newBlockingStub(channel);
		MatrixReply A=stub.addBlock(MatrixRequest.newBuilder()
			.setA00(1)
			.setA01(2)
			.setA10(5)
			.setA11(6)
			.setB00(2)
			.setB01(3)
			.setB10(6)
			.setB11(7)
			.build());
		String resp=A.getC00()+A.getC01()+A.getC10()+A.getC11()+"";
		return resp;
    }

	public String multMatrix(){
                ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext()
                .build();
	       MatrixServiceGrpc.MatrixServiceBlockingStub stub
                 = MatrixServiceGrpc.newBlockingStub(channel);
                MatrixReply A=stub.multiplyBlock(MatrixRequest.newBuilder()
                        .setA00(1)
                        .setA01(2)
                        .setA10(5)
                        .setA11(6)
                        .setB00(2)
                        .setB01(3)
                        .setB10(6)
                        .setB11(7)
                        .build());
                String resp=A.getC00()+A.getC01()+A.getC10()+A.getC11()+"";
		return resp;

	}

	public String fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("file2") MultipartFile file2){
		String content = new String(file.getBytes(), StandardCharsets.UTF_8);
		System.out.println("File 1 uploaded correctlu");
		System.out.println(content);


 		String content2 = new String(file2.getBytes(), StandardCharsets.UTF_8);
                System.out.println("File 2 uploaded correctlu");
                System.out.println(content2);
    		return content + content2 +  "hello" ;
        	
   	 }
}
