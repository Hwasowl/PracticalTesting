package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

//@SpringBootTest // 모든 설정을 로드하여 테스트
@DataJpaTest // JPA 관련 설정만 로드하여 테스트
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn() {
        // given
        Product americano = createProduct("001", ProductType.HANDMADE, 4000, ProductSellingStatus.SELLING, "아메리카노");
        Product cafeLatte = createProduct("001", ProductType.HANDMADE, 4500, ProductSellingStatus.HOLD, "카페라뗴");
        Product shavedIce = createProduct("001", ProductType.HANDMADE, 7000, ProductSellingStatus.STOP_SELLING, "팥빙수");
        productRepository.saveAll(List.of(americano, cafeLatte, shavedIce));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD));

        // then
        assertThat(products)
            .hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", ProductSellingStatus.SELLING), tuple("001", "카페라뗴", ProductSellingStatus.HOLD)
            )
        ;
    }

    @Test
    @DisplayName("상품 번호 리스트로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product americano = createProduct("001", ProductType.HANDMADE, 4000, ProductSellingStatus.SELLING, "아메리카노");
        Product cafeLatte = createProduct("002", ProductType.HANDMADE, 4500, ProductSellingStatus.HOLD, "카페라뗴");
        Product shavedIce = createProduct("003", ProductType.HANDMADE, 7000, ProductSellingStatus.STOP_SELLING, "팥빙수");
        productRepository.saveAll(List.of(americano, cafeLatte, shavedIce));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", ProductSellingStatus.SELLING), tuple("002", "카페라뗴", ProductSellingStatus.HOLD)
            );
    }

    @Test
    @DisplayName("가장 마지막으로 저장 된 상품 번호를 조회한다")
    void findLatestProduct() {
        // given
        Product americano = createProduct("001", ProductType.HANDMADE, 4000, ProductSellingStatus.SELLING, "아메리카노");
        Product cafeLatte = createProduct("002", ProductType.HANDMADE, 4500, ProductSellingStatus.HOLD, "카페라뗴");
        Product shavedIce = createProduct("003", ProductType.HANDMADE, 7000, ProductSellingStatus.STOP_SELLING, "팥빙수");
        productRepository.saveAll(List.of(americano, cafeLatte, shavedIce));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo("003");
    }

    @Test
    @DisplayName("가장 마지막으로 저장 된 상품 번호를 조회할 때, 상품이 하나도 없는 경우 null을 반환한다.")
    void findLatestProductWhenProductIsEmpty() {
        // given

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductType type, int price, ProductSellingStatus sellingStatus, String name) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(sellingStatus)
            .name(name)
            .build();
    }
}
