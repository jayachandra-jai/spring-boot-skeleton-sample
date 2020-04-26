package com.jai.mystarter.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mohit on 27/8/15.
 */
@Service
public class RestClientService {

    @Value("${RestClient.ThreadPoolSize}")
    private static String RestClientThreadPoolSize;

    @Value("${RestClient.read_timeout}")
    private static String read_timeout;

    @Value("${RestClient.connection_timeout}")
    private static String connection_timeout;

    private static Logger LOGGER = LoggerFactory.getLogger(RestClientService.class);

    static int RestClientThreadPoolSizeVal=(RestClientThreadPoolSize!=null)?Integer.parseInt(RestClientThreadPoolSize):10;
    static int read_timeout_val=(read_timeout!=null)?Integer.parseInt(read_timeout):10;
    static int connection_timeout_val=(connection_timeout!=null)?Integer.parseInt(connection_timeout):10;
    private static ExecutorService executorService = Executors.newFixedThreadPool(RestClientThreadPoolSizeVal);
    private static ThreadLocal<Client> threadLocalClient = new ThreadLocal<Client>() {
        @Override
        protected Client initialValue() {
            Client client = Client.create();

            client.setReadTimeout( read_timeout_val* 1000);
            client.setConnectTimeout(connection_timeout_val * 1000);
            return client;
        }
    };

    public static Object sendJsonPost(final String json, final String url,Map<String,String> headers,String type) {
            LOGGER.info("Update Type: "+type+" URL "+url+" headers"+headers);
            long startMillis = System.currentTimeMillis();
            String apiResponse = null;
            ClientResponse response = null;
            Boolean error=false;
            try {
                Client client = threadLocalClient.get();
                WebResource webResource = client.resource(url);
                webResource.accept(MediaType.APPLICATION_JSON).header("content-type", "application/json");
                if(null!=headers){
                    for (String header: headers.keySet()) {
                        webResource.header(header,headers.get(header));
                    }
                }

                response =webResource.post(ClientResponse.class, json);

                if (response.getStatus() == 200) {
                    apiResponse = response.getEntity(String.class);
                } else {
                    LOGGER.info("Response code "+response.getStatus());
                    error=true;
                }

            } catch (Exception e) {
                LOGGER.error("Error ",e);
                error=true;

            } finally {
//                FileTracker tracker = new FileTracker("http_api_tracker");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("api", url);
                map.put("json", json);
                map.put("type", type);
                map.put("error", error);
                map.put("timeInMillis", Long.toString(System.currentTimeMillis() - startMillis));
                map.put("result", apiResponse);
//                tracker.createMap(map);
//                tracker.writeInfile();

                LOGGER.info("Closing connection");
                if (response != null) {
                    response.close();
                }
                LOGGER.info("Connection closed successfully");
            }

            return apiResponse;

    }
    public void sendJsonPostAysnc(final String json, final String url,Map<String,String> headers,String type) {
        try {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Object res=sendJsonPost(json,url,headers,type);
                    LOGGER.info("sendJsonPostAysnc",res);
                }
            });
        }catch (Exception e){
            LOGGER.info(" Error in sendJsonPostAysnc",e);
        }
    }


    public Object sendGetRequest(final String url,Map<String,String> headers,String type) {
        try {
            LOGGER.info("Entered sendGetRequest() with url : " + url );
            long startMillis = System.currentTimeMillis();
            Object apiResponse = null;
            ClientResponse response = null;
            try {
                Client client = Client.create();
                WebResource webResource = client.resource(url);
                webResource.accept(MediaType.APPLICATION_JSON).header("content-type", "application/json");
                if(null!=headers){
                    for (String header: headers.keySet()) {
                        webResource.header(header,headers.get(header));
                    }
                }
                response = webResource
                            .accept(MediaType.APPLICATION_JSON)
                            .header("content-type", "application/json")
                            .get(ClientResponse.class);

                if (response.getStatus() == 200) {
                    apiResponse = response.getEntity(String.class);
                    LOGGER.info("Response: " + apiResponse);
                } else {
                    LOGGER.error("sendGetRequest() : Failed : HTTP error code : " + response.getStatus() );
                }

            } catch (Exception e) {
                LOGGER.error("sendGetRequest() :: Exception occurred.. " +url + ",cause: ", e);
            } finally {
                try {
//					FileTracker tracker = new FileTracker("http_api_tracker");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("api", url);
                    map.put("timeInMillis", Long.toString(System.currentTimeMillis() - startMillis));
                    map.put("result", apiResponse);
                    map.put("type",type);

//					tracker.createMap(map);
//					tracker.writeInfile();

                    LOGGER.info("Closing connection");
                    LOGGER.info("HttpApiResponse"+map);
                    if (response != null) {
                        response.close();
                    }
                    LOGGER.info("Connection closed successfully");
                } catch (Exception e) {
                    LOGGER.error("Error closing connection");
                }
            }


            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("Error closing connection", e);
            return null;
        }
    }

    public void  sendGetRequestAysnc(final String url,Map<String,String> headers,String type) {
        try {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Object res=sendGetRequest(url,headers,type);
                    LOGGER.info("sendGetRequestAysnc",res);
                }
            });
        }catch (Exception e){
            LOGGER.info(" Error in sendGetRequestAysnc",e);
        }
    }




}