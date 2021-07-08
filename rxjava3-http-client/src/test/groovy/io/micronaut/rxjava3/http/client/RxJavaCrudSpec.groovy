package io.micronaut.rxjava3.http.client

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import spock.lang.Specification

import jakarta.inject.Inject
import java.util.concurrent.atomic.AtomicLong

/**
 * @author graemerocher
 * @since 1.0
 */
@MicronautTest
class RxJavaCrudSpec extends Specification {

    @Inject
    BookClient client

    @Inject
    @Client("/")
    Rx3HttpClient rx3HttpClient

    void "test it is possible to implement CRUD operations with RxJava"() {


        when:
        def result = client.get(99)
                .blockingGet()
        Book book = result
        List<Book> books = client.list().blockingGet()


        then:
        book == null
        books.size() == 0
        client.flow().toList().blockingGet().size() == 0

        when:
        book = client.save("The Stand").blockingGet()

        then:
        book != null
        book.title == "The Stand"
        book.id == 1

        when:
        book = client.get(book.id).blockingGet()

        then:
        book != null
        book.title == "The Stand"
        book.id == 1
        client.flow().toList().blockingGet().size() == 1
        rx3HttpClient.retrieve(HttpRequest.GET("/rxjava/books/" + book.id), Book).blockingFirst()
            .title == "The Stand"


        when:'the full response is resolved'
        HttpResponse<Book> bookAndResponse = client.getResponse(book.id).blockingGet()

        then:"The response is valid"
        bookAndResponse.status() == HttpStatus.OK
        bookAndResponse.body().title == "The Stand"

        when:
        book = client.update(book.id, "The Shining").blockingGet()

        then:
        book != null
        book.title == "The Shining"
        book.id == 1

        when:
        book = client.delete(book.id).blockingGet()

        then:
        book != null

        when:
        book = client.get(book.id)
                .blockingGet()
        then:
        book == null
    }


    @Client('/rxjava/books')
    static interface BookClient extends BookApi {
    }

    @Controller("/rxjava/books")
    static class BookController implements BookApi {

        Map<Long, Book> books = new LinkedHashMap<>()
        AtomicLong currentId = new AtomicLong(0)

        @Override
        Maybe<Book> get(Long id) {
            Book book = books.get(id)
            if(book)
                return Maybe.just(book)
            Maybe.empty()
        }

        @Override
        Single<HttpResponse<Book>> getResponse(Long id) {
            Book book = books.get(id)
            if(book) {
                return Single.just(HttpResponse.ok(book))
            }
            return Single.just(HttpResponse.notFound())
        }

        @Override
        Single<List<Book>> list() {
            return Single.just(books.values().toList())
        }

        @Override
        Flowable<Book> flow() {
            return Flowable.fromIterable(books.values())
        }

        @Override
        Maybe<Book> delete(Long id) {
            Book book = books.remove(id)
            if(book) {
                return Maybe.just(book)
            }
            return Maybe.empty()
        }

        @Override
        Single<Book> save(String title) {
            Book book = new Book(title: title, id:currentId.incrementAndGet())
            books[book.id] = book
            return Single.just(book)
        }

        @Override
        Maybe<Book> update(Long id, String title) {
            Book book = books[id]
            if(book != null) {
                book.title = title
                return Maybe.just(book)
            }
            else {
                return Maybe.empty()
            }
        }
    }

    static interface BookApi {

        @Get("/{id}")
        Maybe<Book> get(Long id)

        @Get("/res/{id}")
        Single<HttpResponse<Book>> getResponse(Long id)

        @Get
        Single<List<Book>> list()

        @Get("/flow")
        Flowable<Book> flow()

        @Delete("/{id}")
        Maybe<Book> delete(Long id)

        @Post
        Single<Book> save(String title)

        @Patch("/{id}")
        Maybe<Book> update(Long id, String title)
    }


    static class Book {
        Long id
        String title
    }
}
