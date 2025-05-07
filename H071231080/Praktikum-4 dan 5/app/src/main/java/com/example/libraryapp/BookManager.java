package com.example.libraryapp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookManager {
    private static BookManager instance;
    private List<Book> books;
    private List<String> predefinedGenres;


    private BookManager() {
        books = new ArrayList<>();
        predefinedGenres = new ArrayList<>(); // Inisialisasi daftar genre
        initPredefinedGenres();
        initDummyBooks();
    }

    // untuk memastikan hanya satu instance dari BooksDataSource digunakan selama aplikasi berjalan
   public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    // Inisialisasi genre yang sudah ada
    private void initPredefinedGenres() {
        predefinedGenres.add("Drama");
        predefinedGenres.add("Romance");
        predefinedGenres.add("Horror");
        predefinedGenres.add("Fantasy");
        predefinedGenres.add("Science Fiction");
        predefinedGenres.add("Thriller");
    }

    // Mengembalikan semua buku dengan urutan tanggal penambahan terbaru
    public List<Book> getAllBooks() {
        List<Book> sortedBooks = new ArrayList<>(books);
        Collections.sort(sortedBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book1, Book book2) {
                return book2.getAddedDate().compareTo(book1.getAddedDate());
            }
        });
        return sortedBooks;
    }

    // Mengambil semua buku yang ditandai sebagai favorit
    public List<Book> getFavoriteBooks() {
        List<Book> favoriteBooks = new ArrayList<>();
        for (Book book : books) {
            // Cek apakah buku ditandai sebagai favorit
            if (book.isFavorite()) {
                favoriteBooks.add(book);
            }
        }
        return favoriteBooks;
    }

    public List<Book> searchBooks(String query) {
        List<Book> searchResults = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerCaseQuery)) {
                searchResults.add(book);
            }
        }

        return searchResults;
    }

    public List<Book> getBooksByGenre(String genre) {
        List<Book> genreBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                genreBooks.add(book);
            }
        }
        return genreBooks;
    }

    public List<String> getAllGenres() {
        return new ArrayList<>(predefinedGenres); // Mengembalikan daftar genre tetap
    }

    public void addBook(Book book) {
        // Verifikasi apakah genre buku ada dalam daftar genre yang telah ditentukan
        boolean isValidGenre = false;
        for (String genre : predefinedGenres) {
            if (genre.equalsIgnoreCase(book.getGenre())) {
                isValidGenre = true;
                break;
            }
        }

        if (isValidGenre) {
            books.add(book);
        }
    }

    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    private void initDummyBooks() {
        String defaultCoverUri = "android.resource://com.example.libraryapp/drawable/book_default";

        try {
            addBook(new Book("Kata: Tentang Senja yang Kehilangan Langitnya", "Rintik Sedu", 2018,
                    "Mengisahkan cinta segitiga antara Binta, Biru, dan Nugraha. Binta, yang memiliki luka masa lalu, terjebak antara dua pria yang mencintainya dengan cara berbeda.",
                    "kata_cover", "Drama"));

            addBook(new Book("Geez & Ann #1", "Rintik Sedu",  2017,
                    "Kisah cinta remaja antara Geez dan Ann yang penuh dengan dinamika hubungan, perbedaan karakter, dan tantangan jarak.",
                    "geezann1", "Romance"));

            addBook(new Book(" Geez & Ann #2", "Rintik Sedu", 2017,
                    "Kelanjutan kisah Geez dan Ann, menghadirkan konflik yang lebih kompleks dalam hubungan mereka seiring berjalannya waktu.",
                    "geezann2", "Romance"));

            addBook(new Book("Buku Minta Dibanting", "Rintik Sedu", 2020,
                    "Kelanjutan kisah Geez dan Ann, menghadirkan konflik yang lebih kompleks dalam hubungan mereka seiring berjalannya waktu.",
                    "dibanting", "Romance"));

            addBook(new Book("Masih Ingatkah Kau Jalan Pulang", "Rintik Sedu & Sapardi Djoko Damono", 2020,
                    "Kumpulan tulisan yang menggambarkan perasaan patah hati, harapan, dan kekecewaan dalam hubungan percintaan.",
                    "jalan_pulang", "Drama"));

            addBook(new Book("Buku Minta Disayang", "Rintik Sedu", 2021,
                    " Lanjutan dari \"Buku Minta Dibanting\", berisi tulisan yang lebih hangat dan penuh harapan tentang cinta dan kasih sayang.",
                    "disayang", "Romance"));

            addBook(new Book("Pukul Setengah Lima", "Rintik Sedu", 2023,
                    "Menceritakan Alina yang lelah dengan dirinya sendiri dan memilih berpura-pura menjadi orang lain untuk menemukan kebahagiaan.",
                    "setengah_lima", "Drama"));

            addBook(new Book("Danur: I Can See Ghosts", "Risa Saraswati", 2011,
                    "Kisah nyata pengalaman Risa Saraswati yang sejak kecil mampu melihat dan berinteraksi dengan makhluk tak kasat mata, terutama lima sahabat hantu Belanda yang menjadi bagian hidupnya.",
                    "danur", "Thriller"));

            addBook(new Book("Maddah: Danur 2", "Risa Saraswati", 2012,
                    "Kelanjutan kisah Danur, Risa kembali menghadapi teror dari dunia lain yang semakin menegangkan dan penuh misteri.",
                    "maddah", "Thriller"));

            addBook(new Book("Sunyaruri", "Risa Saraswati", 2015,
                    "Kisah perjalanan batin Risa Saraswati dalam menghadapi kesepian dan pertanyaan hidup, dibalut suasana mistis dan reflektif.",
                    "sunyaruri", "Thriller"));

            addBook(new Book("Jurnal Risa: Teror Liburan Sekolah", "Risa Saraswati", 2019,
                    "Kumpulan kisah seram yang dialami Risa dan teman-temannya saat liburan sekolah, penuh kejutan dan misteri.",
                    "teror_liburan", "Thriller"));

            addBook(new Book("Jurnal Risa: William", "Risa Saraswati", 2018,
                    "Kisah khusus tentang William, salah satu sahabat hantu Risa. Buku ini mengungkap latar belakang kehidupan dan kematian William serta hubungannya dengan Risa.",
                    "william", "Horror"));

            addBook(new Book("Jurnal Risa: Peter", "Risa Saraswati", 2018,
                    "Cerita mendalam tentang Peter, pemimpin dari sahabat hantu Risa. Peter digambarkan sebagai sosok pelindung bagi teman-temannya serta Risa.",
                    "peter", "Horror"));

            addBook(new Book("Jurnal Risa: Hans", "Risa Saraswati", 2018,
                    "Mengisahkan Hans, salah satu sahabat hantu Risa yang dikenal ceria dan penuh kehangatan. Hans sering menghibur Risa di masa kecilnya.",
                    "hans", "Horror"));

            addBook(new Book("Jurnal Risa: Hendrick", "Risa Saraswati", 2018,
                    "Kisah tentang Hendrick, sahabat hantu Risa yang pendiam namun sangat setia. Buku ini mengungkap sisi lembut Hendrick dan kisah masa lalunya.",
                    "hendrick", "Horror"));

            addBook(new Book("Jurnal Risa: Janshen", "Risa Saraswati", 2018,
                    "Cerita tentang Janshen, anggota termuda di antara sahabat hantu Risa. Janshen dikenal polos dan sering membuat suasana menjadi ceria.",
                    "janshen", "Horror"));
        } catch (IllegalArgumentException e) {
            // Log error jika terjadi masalah saat menambahkan buku dummy
            e.printStackTrace();
        }
    }
}