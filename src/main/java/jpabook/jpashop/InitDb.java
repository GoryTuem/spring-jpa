package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final IninService ininService;
    @PostConstruct
    public void init(){
        ininService.dbInit1();
        ininService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class IninService{
        private final EntityManager em;
        public void dbInit1(){
            Member member = createMember("userA","서울","1","12234");
            em.persist(member);

            Book book = getBook("JPA1 BOOK",10000,100);
            em.persist(book);

            Book book2 = getBook("JPA2 BOOK",20000,100);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem, orderItem2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = createMember("userB", "진주", "2", "2222");
            em.persist(member);

            Book book = getBook("SPRING1 BOOK", 20000, 200);
            em.persist(book);

            Book book2 = getBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = getDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem, orderItem2);
            em.persist(order);
        }

        private static Delivery getDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book getBook(String name, int price,int stock) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stock);
            return book;
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }
    }
}
