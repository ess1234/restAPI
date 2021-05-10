package com.navercorp.micro;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@RestController
@RequestMapping("/orderInfo")
public class OrderInfoController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ParametersUtil parametersUtil;
	
    ArrayList<JSONObject> orderInfo = new ArrayList<JSONObject>();
	
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

	
	/*
	 * POS => micro 로 정보 입력
	 * parameter -> parsing 방식
	 * */	
	@PostMapping
    public ResponseEntity<?> pushOrderInfo(HttpServletRequest httpServletRequest) {
		
		HashMap<String, String> param = this.parametersUtil.generateParameter(httpServletRequest);
		logger.info("pushOrderInfo" + param.get("JSON"));
		
		// array 로 전달 받음
		JSONArray orders = JSONArray.fromObject(param.get("JSON"));
		for(int i=0; i<orders.size(); i++) {
			JSONObject order = orders.getJSONObject(i);
			logger.info("pushOrderInfo" + order.get("posNo"));
			orderInfo.add(order);
						
			ListOperations<String, Object> valueOps =  redisTemplate.opsForList();
			Order o = new Order();
			o.setPosNo((String) order.get("posNo"));
			valueOps.leftPushAll("orderInfo", o);
			
//			ValueOperations<String, Object> vop = redisTemplate.opsForValue();
//			Order o = new Order();
//			o.setPosNo((String) order.get("posNo"));
//			vop.set("key", o);
		}
//		return new ResponseEntity<>("There's no posNo.", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>("added", HttpStatus.OK);
    }
	
	/*
	 * POS => micro 로 정보 입력
	 * post 방식
	 * */	
//	@PostMapping
//    public ResponseEntity<?> pushOrderInfo(@RequestBody JSONArray orders) {
//		for(int i=0; i<orders.size(); i++) {
//			JSONObject order = orders.getJSONObject(i);
//			logger.debug("pushOrderInfo" + order.get("posNo"));
//			orderInfo.add(order);
//		}
//        // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);		
//        return new ResponseEntity<>("added", HttpStatus.OK);
//    }
	
	/*
	 * micro => client 로 정보 호출
	 * */
	@GetMapping
    public ResponseEntity<?> pullOrderInfo() {		
		ListOperations<String, Object> valueOps =  redisTemplate.opsForList();
		logger.info("data :: " +valueOps.size("orderInfo") );
		for(int i=0; i<valueOps.size("orderInfo") ; i++) {
			Order o = (Order)valueOps.index("orderInfo", i);
			logger.info("data :: " +o.getPosNo() );
		}
		
        return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    }

}
