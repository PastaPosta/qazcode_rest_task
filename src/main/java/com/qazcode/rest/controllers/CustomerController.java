package com.qazcode.rest.controllers;

import com.qazcode.rest.entities.Customer;
import com.qazcode.rest.services.CustomerServiceInterface;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "Управление данными клиентов")
public class CustomerController {

    private final CustomerServiceInterface customerService;

    @GetMapping
    @Operation(summary = "Получить всех клиентов", description = "Возвращает полный список клиентов из базы данных")
    public List<Customer> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти клиента по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "404", description = "Клиент с таким ID не существует")
    })
    public Customer getById(@Parameter(description = "Идентификатор клиента") @PathVariable Long id) {
        return customerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового клиента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или email уже занят")
    })
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer.getName(), customer.getEmail());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные клиента")
    public Customer updateById(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить клиента", description = "Удаление невозможно, если у клиента есть заказы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Клиент удален"),
            @ApiResponse(responseCode = "400", description = "Нельзя удалить клиента с активными заказами")
    })
    public void deleteById(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}