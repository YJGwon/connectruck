package com.connectruck.foodtruck.order.domain;

import static com.connectruck.foodtruck.order.domain.OrderStatus.COOKING;
import static com.connectruck.foodtruck.order.domain.OrderStatus.CREATED;
import static lombok.AccessLevel.PROTECTED;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.validation.Validator;
import com.connectruck.foodtruck.order.exception.IllegalOrderStatusException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_info_id")
    @Include
    private Long id;

    private Long truckId;
    private String phone;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "orderInfo", cascade = CascadeType.PERSIST)
    private List<OrderLine> orderLines;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static OrderInfo ofNew(final Long truckId, final String phone) {
        Validator.validatePhone(phone);
        return new OrderInfo(null, truckId, phone, CREATED, new ArrayList<>(), null, null);
    }

    public void changeOrderLine(final List<OrderLine> orderLines) {
        checkOrderLinesNotEmpty(orderLines);
        this.orderLines = List.copyOf(orderLines);
    }

    public void accept() {
        checkStatusAcceptable();
        status = COOKING;
    }

    private void checkOrderLinesNotEmpty(final List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new ClientException("주문 메뉴 내역을 변경할 수 없습니다.", "주문 메뉴 내역을 빈 값으로 변경할 수 없습니다.");
        }
    }

    private void checkStatusAcceptable() {
        if (!status.isAcceptable()) {
            throw IllegalOrderStatusException.ofUnacceptable(status);
        }
    }
}
