package com.connectruck.foodtruck.order.domain;

import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.validation.Validator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_info_id")
    @Include
    private Long id;

    private String phone;

    @OneToMany(mappedBy = "orderInfo", cascade = CascadeType.PERSIST)
    private List<OrderLine> orderLines;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public OrderInfo(final Long id, final String phone, final List<OrderLine> orderLines, final LocalDateTime createdAt,
                     final LocalDateTime updatedAt) {
        Validator.validatePhone(phone);
        this.id = id;
        this.phone = phone;
        this.orderLines = orderLines;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static OrderInfo ofNew(final String phone) {
        return new OrderInfo(null, phone, new ArrayList<>(), null, null);
    }

    public void changeOrderLine(final List<OrderLine> orderLines) {
        checkOrderLinesNotEmpty(orderLines);
        this.orderLines = List.copyOf(orderLines);
    }

    private void checkOrderLinesNotEmpty(final List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new ClientException("주문 메뉴 내역을 변경할 수 없습니다.", "주문 메뉴 내역을 빈 값으로 변경할 수 없습니다.");
        }
    }
}
