package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);
        Item item = new Book();
        item.setName("트렌드 2024");
        item.setPrice(16000);
        item.setStockQuantity(100);
        em.persist(item);

        int orderCount = 3;
        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 order", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야한다.",1,findOrder.getOrderItems().size());
        assertEquals("상품 가격은 가격 * 수량이다",16000*orderCount,findOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 감소되어야한다.",100-orderCount,item.getStockQuantity());

    }
    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);
        Item item = new Book();
        item.setName("트렌드 2024");
        item.setPrice(16000);
        item.setStockQuantity(100);
        em.persist(item);

        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문상태가 cancel 어이야한다.",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("상품 재고가 원복 되어야함.",100,item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강서구","123-123"));
        em.persist(member);
        Item item = new Book();
        item.setName("트렌드 2024");
        item.setPrice(16000);
        item.setStockQuantity(10);
        em.persist(item);

        int orderCount = 11;

        //when
        orderService.order(member.getId(),item.getId(),orderCount);
        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");

    }


}