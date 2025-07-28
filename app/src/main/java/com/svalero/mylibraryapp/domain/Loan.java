package com.svalero.mylibraryapp.domain;

import java.time.LocalDate;

/**
 * Representa un préstamo de libros a un cliente.
 * Esta clase puede ser usada para enviar/recibir datos desde la API.
 * ⚠️ LocalDate debe ser serializable con formato ISO en tu JSON mapper (ej. Gson, Moshi).
 */
public class Loan {

    private String name;           // Nombre del préstamo (ej. "Préstamo mensual")
    private String customerName;   // Nombre del cliente
    private String email;          // Email del cliente
    private LocalDate loanDate;    // Fecha del préstamo
    private int quantity;          // Número de libros prestados

    // --- Constructor habitual ---
    public Loan(String name, String customerName, String email, LocalDate loanDate, int quantity) {
        this.name = name;
        this.customerName = customerName;
        this.email = email;
        this.loanDate = loanDate;
        this.quantity = quantity;
    }

    // --- Getters y setters para cada campo ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // --- toString para depurar ---
    @Override
    public String toString() {
        return "Loan{" +
                "name='" + name + '\'' +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", loanDate=" + loanDate +
                ", quantity=" + quantity +
                '}';
    }
}
