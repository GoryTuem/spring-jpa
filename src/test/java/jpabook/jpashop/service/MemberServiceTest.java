package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;
    @Test
    @Rollback(false) // 테스트 완료 후 롤백 안 됨.
    public void 회원가입() throws Exception{
        //given
        Member member= new Member();
        member.setName("문소희");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findMember(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("최강호");

        Member member2 = new Member();
        member2.setName("최강호");
        //when
        memberService.join(member1);
        memberService.join(member2); // 중복된 경우 예외!

        //then
        fail("예외가 발생");
    }

}