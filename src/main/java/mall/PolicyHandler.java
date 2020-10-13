package mall;

import mall.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    
    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_UpdateStatus(@Payload Shipped shipped){

        if(shipped.isMe()){

            // 업데이트 위한 orderId를 통해 id 찾기
            Optional<Order> orderOptional = orderRepository.findById(shipped.getOrderId());
                // findById 리턴 타입이 Optional<> 이네...
            Order order = orderOptional.get();  // 해당 주문 1건 추출된 데이터 Order로 가져오긴
            order.setStatus(shipped.getStatus());
            
            orderRepository.save(order);
            // 이후 업데이트 트리거 처리 필요  > PostPersist로 처리가 안됨
            
            System.out.println("##### listener UpdateStatus : " + shipped.toJson());
        }
    }

}
