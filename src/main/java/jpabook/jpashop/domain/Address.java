package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter //값타입은 변경 불가능하게 설계!
public class Address {
    private String city;
    private  String street;
    private String zipcode;

    protected Address(){} // 생성자는 public 또는 protected 사용
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
