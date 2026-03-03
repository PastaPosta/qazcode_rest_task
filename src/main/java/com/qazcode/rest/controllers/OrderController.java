package com.qazcode.rest.controllers;

import com.qazcode.rest.entities.Order;
import com.qazcode.rest.services.OrderServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "Управление заказами и их статусами")
public class OrderController {

    private final OrderServiceInterface orderService;

    @GetMapping
    @Operation(summary = "Список всех заказов с пагинацией и сортировкой")
    public List<Order> getAll(
            @Parameter(description = "Номер страницы (с 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки (например: id, amount, createdAt)")
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return orderService.getAllOrders(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по номеру")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    public Order getById(@PathVariable Long id){
        return orderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый заказ", description = "Заказ создается в статусе NEW")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ создан"),
            @ApiResponse(responseCode = "404", description = "Указанный заказ не найден"),
            @ApiResponse(responseCode = "400", description = "Сумма заказа должна быть больше 0")
    })
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderService.createOrder(order.getCustomerId(), order.getAmount());
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "Оплатить заказ", description = "Переводит статус из NEW в PAID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная оплата"),
            @ApiResponse(responseCode = "400", description = "Заказ уже оплачен или отменен")
    })
    public Order payOrder(@PathVariable Long id) {
        return orderService.payOrder(id);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Отменить заказ", description = "Переводит статус в CANCELLED. Оплаченные заказы отменять нельзя.")
    public Order cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить сумму заказа", description = "Доступно только для заказов в статусе NEW")
    public Order updateById(@PathVariable Long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order.getAmount());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить заказ")
    public void deleteById(@PathVariable Long id) {
        orderService.removeOrder(id);
    }
}