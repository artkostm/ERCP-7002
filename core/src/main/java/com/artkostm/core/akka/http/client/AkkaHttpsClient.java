package com.artkostm.core.akka.http.client;

import java.util.Arrays;

import scala.concurrent.Future;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.http.javadsl.Http;
import akka.http.javadsl.OutgoingConnection;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;





//import com.artkostm.core.akka.http.client.AkkaHttpClient.Completing;

public class AkkaHttpsClient
{
//    public static void main(String[] args)
//    {
//        final ActorSystem system = ActorSystem.create("akka-http-client");
//        final ActorMaterializer materializer = ActorMaterializer.create(system);
//        final Http http = Http.get(system);
//        http.defaultClientHttpsContext();
//        final Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow = 
//                http.outgoingConnectionTls("raw.githubusercontent.com", 443);
//        final HttpRequest request = HttpRequest.create("/artkostm/ERCP-7002/master/core/src/main/java/com/artkostm/core/akka/http/client/AkkaHttpsClient.java");
//        
//        final Future<HttpResponse> responseFuture =  Source.from(Arrays.asList(request))
//                .via(connectionFlow)
//                .runWith(Sink.<HttpResponse>head(), materializer);
//        
//        final OnComplete<HttpResponse> onComplete = new Completing(system, materializer, true);
//
//        responseFuture.onComplete(onComplete, system.dispatcher());
//        //test1(system, materializer);
//    }
//
//    public static void test1(final ActorSystem system, final ActorMaterializer materializer)
//    {
//        final long start = System.currentTimeMillis();
//        final Source<Integer, BoxedUnit> source = Source.range(1, Integer.MAX_VALUE);
////                .mapAsyncUnordered(10, integer -> 
////        { 
////            return Futures.future(new Callable<Integer>()
////            {
////                @Override
////                public Integer call() throws Exception
////                {
////                    return integer + 1;
////                }
////            }, system.dispatcher()); 
////        });
//        
//        final Sink<Integer, Future<Long>> sink = Sink.<Long, Integer>fold(0L, (s, i) -> s + i);
//        final RunnableGraph<Future<Long>> graph = source.toMat(sink, Keep.right());
//        
//        final Future<Long> future = graph.run(materializer);
//        future.onComplete(new OnComplete<Long>()
//        {
//            @Override
//            public void onComplete(Throwable arg0, Long result) throws Throwable
//            {
//                System.out.println(result);
//                System.out.println((System.currentTimeMillis() - start) + "ms");
//                system.terminate();
//            }
//        }, system.dispatcher());
//    }
}
