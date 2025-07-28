package com.svalero.mylibraryapp.api;

import com.svalero.mylibraryapp.domain.AuthRequest;
import com.svalero.mylibraryapp.domain.AuthResponse;
import com.svalero.mylibraryapp.domain.Book;
import com.svalero.mylibraryapp.domain.BookCategory;
import com.svalero.mylibraryapp.domain.Loan;
import com.svalero.mylibraryapp.domain.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BooksApiInterface {
    // --- Auth ---
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest credentials);

    // --- Usuarios ---
    @GET("users")
    Call<List<User>> getAllUsers(
            @Query("active") Boolean active,
            @Query("username") String username,
            @Query("email") String email
    );

    @GET("users/search")
    Call<List<User>> findUsersByName(@Query("name") String name);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") long id);

    @POST("users")
    Call<User> addUser(@Body User user);

    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") long id, @Body User user);

    @PATCH("users/{id}")
    Call<User> patchUser(@Path("id") long id, @Body Map<String, Object> fields);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") long id);

    // --- Categorías de libro ---
    @GET("book-categories")
    Call<List<BookCategory>> getAllCategories(
            @Query("active") Boolean active,
            @Query("createdDate") String createdDate,
            @Query("name") String name
    );

    @GET("book-categories/{id}")
    Call<BookCategory> getCategory(@Path("id") long id);

    @POST("book-categories")
    Call<BookCategory> addCategory(@Body BookCategory category);

    @PUT("book-categories/{id}")
    Call<BookCategory> updateCategory(@Path("id") long id, @Body BookCategory category);

    @PATCH("book-categories/{id}")
    Call<BookCategory> patchCategory(@Path("id") long id, @Body Map<String, Object> fields);

    @DELETE("book-categories/{id}")
    Call<Void> deleteCategory(@Path("id") long id);



    // --- Libros ---
    @GET("books")
    Call<List<Book>> getAllBooks(
            @Query("genre") String genre,
            @Query("available") Boolean available,
            @Query("minPages") Integer minPages
    );

    @GET("books/{id}")
    Call<Book> getBook(@Path("id") long id);

    @POST("books")
    Call<Book> addBook(@Body Book book);

    @PUT("books/{id}")
    Call<Book> updateBook(@Path("id") long id, @Body Book book);

    @PATCH("books/{id}")
    Call<Book> patchBook(@Path("id") long id, @Body Map<String, Object> fields);

    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") long id);

    // --- Préstamos (Loans) ---
    @GET("loans")
    Call<List<Loan>> getAllLoans(
            @Query("email") String email,
            @Query("loanDate") String loanDate,
            @Query("quantity") Integer quantity
    );

    @GET("loans/quantity/greater")
    Call<List<Loan>> getLoansByMinQuantity(@Query("quantity") int quantity);

    @GET("loans/{id}")
    Call<Loan> getLoan(@Path("id") long id);

    @POST("loans")
    Call<Loan> addLoan(@Body Loan loan);

    @PUT("loans/{id}")
    Call<Loan> updateLoan(@Path("id") long id, @Body Loan loan);

    @PATCH("loans/{id}")
    Call<Loan> patchLoan(@Path("id") long id, @Body Map<String, Object> fields);

    @DELETE("loans/{id}")
    Call<Void> deleteLoan(@Path("id") long id);
}
