package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CafeKioskTest {
    @Test
    void addManual() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);
//        System.out.println(">>> 음료 수 : " + cafeKiosk.getBeverages().size());
    }

    @Test
    @DisplayName("음료 1개 추가하면 주문 목록에 담긴다.")
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상만 주문하실 수 있습니다.");
    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);
        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);
        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);
        Order order = cafeKiosk.createOrderWithCurrentTime(LocalDateTime.of(2023, 5, 15, 10, 0));
        assertThat(order.getBeverages()).hasSize(2);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(order.getBeverages().get(1).getName()).isEqualTo("라떼");
    }

    @Test
    @DisplayName("영업 시작 시간 이전에는 주문을 생성할 수 없다.")
    // [특정 시간 이전에 주문을 생성하면 실패한다.]
    // -> [영업 시작 시간 이전에는 주문을 생성할 수 없다.]
    //  -> 도메인 용어를 사용하여 한층 추상화된 내용을 담고, 테스트의 현상을 중점으로 기술하지 말것.
    void createOrderOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        assertThatThrownBy(() -> cafeKiosk.createOrderWithCurrentTime(LocalDateTime.of(2023, 5, 15, 6, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 주문 가능한 시간이 아닙니다. 관리자에게 문의하세요.");
    }

    @Test
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    void calculateTotalPrice() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(8500);
    }
}
